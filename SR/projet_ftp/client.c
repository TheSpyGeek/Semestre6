

#include "define.h"
#include "csapp.h"
#include "parse.h"

int slave;

void handler_kill(int sig){
	int int_cmd = EXIT;
	write(slave, &int_cmd, sizeof(int));
	close(slave);
	printf("[CLIENT] Deconnection\n");
	exit(0);

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
						read(slave, &info, sizeof(int));

						if(info == OK){
							write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));
							printf("[CLIENT] filename (%s) sent\n", cmd_parsed[1]);
							read(slave, &nb_octets, sizeof(int));
							// printf("nb_octets %d\n", nb_octets);
							if(nb_octets > 0){
								printf("[CLIENT] file : %d bytes\n", nb_octets);

								file = open(cmd_parsed[1], O_WRONLY | O_CREAT, S_IWUSR | S_IRUSR | S_IRGRP  | S_IROTH);

								if(file != -1){

									info = OK;
									write(slave, &info, sizeof(int));
									nb = 0;
									while(nb < nb_octets){
										n = read(slave, buf, MAXLINE);
										nb += n;
										printf("nb : %d\n", nb);
										write(file, buf, n);
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
						}

					} else {
						printf("[CLIENT] Error get syntax : get <filename>\n");
					}


					break;
				default:
					break;
			}

		}



	}



}