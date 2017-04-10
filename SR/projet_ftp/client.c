

#include "csapp.h"
#include "define.h"
#include "parse.h"

int slave;

void handler_kill(int sig){
	int int_cmd = EXIT;
	write(slave, &int_cmd, sizeof(int));
	close(slave);
	printf("\n[CLIENT] Deconnection\n");
	exit(0);

}


int attente_reponse(int fd, int time);

int attente_reponse_tres_court(int fd);

void reception_fichier(int file, int nb, int nb_octets);

int main(int argc, char **argv){

	char host[INET_ADDRSTRLEN];
	int clientfd, listenfd;
	int int_cmd, info;
	char cmd[MAXLINE];
	char filename[MAXLINE];
	char **cmd_parsed;
    socklen_t clientlen;
    struct sockaddr_in clientaddr;
    int nb_octets, n;
    int nb_bytes;
    int file;
    char buf[MAXLINE];

    if(argc < 2){
    	strcpy(host, "localhost");
    } else {
    	strcpy(host, argv[1]);
    }


    signal(SIGINT, handler_kill);

    /// CONNECTION TO MASTER
    clientlen = (socklen_t)sizeof(clientaddr);
	clientfd = Open_clientfd(host, CLIENT_PORT);
	printf("Connected to master\n"); 

	listenfd = Open_listenfd(SLAVE_TO_CLIENT);

	slave = Accept(listenfd, (SA *)&clientaddr, &clientlen);

	close(clientfd);
	printf("Connected to slave\n");

	if(attente_reponse(slave, SHORT_TIMEOUT)){
		read(slave, &info, sizeof(int));

		/// si le client a crash il doit reprendre le téléchargement du fichier
		if(info == CLIENT_CRASHED_DURING_TRANSFERT){

			envoi_info(slave, OK);

			if(attente_reponse(slave, SHORT_TIMEOUT)){
				n = read(slave, filename, MAXLINE);
				filename[n+1] = '\0';

				envoi_info(slave, OK);

				if(attente_reponse(slave, SHORT_TIMEOUT)){
					read(slave, &nb_bytes, sizeof(int));

					envoi_info(slave, OK);

					if(attente_reponse(slave, SHORT_TIMEOUT)){
						read(slave, &nb_octets, sizeof(int));

						file = open(filename, O_WRONLY, S_IWUSR | S_IRUSR | S_IRGRP  | S_IROTH);

						if(file != -1){
							printf("[CLIENT] Transfert of %s start at %d\n", filename, nb_bytes);

							

							reception_fichier(file, nb_bytes, nb_octets);

						} else {
							printf("[CLIENT] Problem during open file\n");
						}
					} else {
						printf("[CLIENT] Slave has crashed\n");
						close(slave);
						exit(0);
					}
				} else {
					printf("[CLIENT] Slave has crashed\n");
					close(slave);
					exit(0);
				}
			} else {
				printf("[CLIENT] Slave has crashed\n");
				close(slave);
				exit(0);
			}

			


		}
	} else {
		printf("[CLIENT] Slave has crashed\n");
		close(slave);
		exit(0);
	}


	while(1){

		printf("ftp > ");

		fgets(cmd, MAXLINE, stdin);

		no_endofline(cmd);
		cmd_parsed = split_in_words(cmd);


		if(cmd_parsed[0] != 0){

			int_cmd = cmd_to_int(cmd_parsed[0]);

			switch(int_cmd){
				case EXIT:
					write(slave, &int_cmd, sizeof(int));
					printf("[CLIENT] Deconnection\n");
					exit(0);
					break;
				case LS:
					write(slave, &int_cmd, sizeof(int));
					// while(attente_reponse_tres_court(slave)){
					while((n = read(slave, buf, MAXLINE)) > 0){
						// n = read(slave, buf, MAXLINE);
						write(STDOUT_FILENO, buf, n);
					}

					break;
				case PWD:
					write(slave, &int_cmd, sizeof(int));
					// while(attente_reponse_tres_court(slave)){
					while((n = read(slave, buf, MAXLINE)) > 0){
						// n = read(slave, buf, MAXLINE);
						write(STDOUT_FILENO, buf, n);
					}
					break;
				case ERROR :
					printf("[CLIENT] Error command unknown\n");
					break;
				case GET : 
					if(cmd_parsed[1] != 0){
						write(slave, &int_cmd, sizeof(int));

						if(attente_reponse(slave, SHORT_TIMEOUT) > 0){
							read(slave, &info, sizeof(int));


							if(info == OK){
								write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));
								printf("[CLIENT] filename (%s) sent\n", cmd_parsed[1]);
								if(attente_reponse(slave, SHORT_TIMEOUT) > 0){
									read(slave, &nb_octets, sizeof(int));
									if(nb_octets > 0){
										printf("[CLIENT] file : %d bytes\n", nb_octets);

										file = open(cmd_parsed[1], O_WRONLY | O_CREAT, S_IWUSR | S_IRUSR | S_IRGRP  | S_IROTH);

										if(file != -1){

											reception_fichier(file, 0, nb_octets);
											
										} else {
											info = ERROR_OPEN;
											write(slave, &info, sizeof(int));
										}


									} else {
										printf("[CLIENT] Error file %s not found\n", cmd_parsed[1]);
									}
								} else {
									printf("[CLIENT] Slave has crashed\n");
									close(slave);
									exit(0);
								}
							}


						} else {
							printf("[CLIENT] Slave has crashed\n");
							close(slave);
							exit(0);
						}
						
					} else {
						printf("[CLIENT] Error get syntax : get <filename>\n");
					}


					break;
				default:
					break;
			}

		}



	} // fin while(1)



}


void reception_fichier(int file, int nb, int nb_octets){
	char buf[MAXLINE];
	int n;

	lseek(file, nb, SEEK_SET);

	printf("[DEBUG] Start at %d bytes has to finish at %d bytes\n", nb, nb_octets);

	envoi_info(slave, OK);

	while(nb < nb_octets){
		if(attente_reponse(slave, SHORT_TIMEOUT) > 0){

			n = read(slave, buf, MAXLINE);
			nb += n;
			write(file, buf, n);

			/// bien reçu
			envoi_info(slave, OK);

		} else {
			printf("[CLIENT] Slave has crashed during the transfert %d bytes : Test %d\n", nb_octets, nb_octets+MAXLINE);
			close(slave);
			exit(0);
		}
	}

	printf("[CLIENT] Transfert successfully done!\n");
	close(file);
}

int attente_reponse_tres_court(int fd){

	fd_set readfd;

	struct timeval timeout;
	timeout.tv_sec = 0;
	timeout.tv_usec = 100;

	FD_ZERO(&readfd);
	FD_SET(fd, &readfd);
	return (select(fd+1, &readfd, 0, 0, &timeout));

}

int attente_reponse(int fd, int time){

	fd_set readfd;

	struct timeval timeout;
	timeout.tv_sec = time;
	timeout.tv_usec = 0;

	FD_ZERO(&readfd);
	FD_SET(fd, &readfd);
	return (select(fd+1, &readfd, 0, 0, &timeout));

}