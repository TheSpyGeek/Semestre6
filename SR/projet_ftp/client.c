

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


int attente_reponse(int fd, int time){

	fd_set readfd;

	struct timeval timeout;
	timeout.tv_sec = time;
	timeout.tv_usec = 0;

	FD_ZERO(&readfd);
	FD_SET(fd, &readfd);
	return (select(fd+1, &readfd, 0, 0, &timeout));

}


int main(int argc, char **argv){

	char host[] = "thespygeek";
	int clientfd, listenfd;
	int int_cmd, info;
	char cmd[MAXLINE];
	char buf[MAXLINE];
	char **cmd_parsed;
    socklen_t clientlen;
    struct sockaddr_in clientaddr;
    int nb_octets, n, nb;
    int file;

    signal(SIGINT, handler_kill);

    /// CONNECTION TO MASTER
    clientlen = (socklen_t)sizeof(clientaddr);
	clientfd = Open_clientfd(host, CLIENT_PORT);
	printf("Connected to master\n"); 

	listenfd = Open_listenfd(SLAVE_TO_CLIENT);

	slave = Accept(listenfd, (SA *)&clientaddr, &clientlen);

	close(clientfd);
	printf("Connected to slave\n");


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
					// while((n = read(slave, buf, MAXLINE)) > 0){
					// 	printf("%s", buf);
					// }

					break;
				case ERROR :
					printf("[CLIENT] Error command unknown\n");
					break;
				case GET : 
					if(cmd_parsed[1] != 0){
						write(slave, &int_cmd, sizeof(int));

						if(attente_reponse(slave, SHORT_TIMEOUT) > 0){

							// printf("passe %s\n", buf);
							read(slave, &info, sizeof(int));
							// read(slave, buf, MAXLINE);

							if(info == OK){
								write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));
								printf("[CLIENT] filename (%s) sent\n", cmd_parsed[1]);
								if(attente_reponse(slave, SHORT_TIMEOUT) > 0){
									read(slave, &nb_octets, sizeof(int));
									if(nb_octets > 0){
										printf("[CLIENT] file : %d bytes\n", nb_octets);

										file = open(cmd_parsed[1], O_WRONLY | O_CREAT, S_IWUSR | S_IRUSR | S_IRGRP  | S_IROTH);

										if(file != -1){

											info = OK;
											write(slave, &info, sizeof(int));
											nb = 0;
											while(nb < nb_octets){
												if(attente_reponse(slave, SHORT_TIMEOUT) > 0){
													n = read(slave, buf, MAXLINE);
													nb += n;
													write(file, buf, n);

													/// bien reçu
													info = OK;
													write(slave, &info, sizeof(int));

												} else {
													printf("[CLIENT] Slave has crashed during the transfert it transfered %d bytes\n", nb);
													close(slave);
													exit(0);
												}
											}

											printf("[CLIENT] Transfert successfully done!\n");
											close(file);
											
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