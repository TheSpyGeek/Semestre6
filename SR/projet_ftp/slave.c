
#include "define.h"
#include "csapp.h"
#include "parse.h"


int master;
int clientfd;




void handler_kill(int sig){
	close(master);
	close(clientfd);
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

	int connected = 0;
	int int_cmd;
	int n, nb, nb_courant, nb_octets, nb_char;
	int info;
	char filename[MAXLINE];
	char buf[MAXLINE];

	struct stat file_stat;

	int file;


	

	// int sortie;


	char *argument[2];

	char client_ip_string[INET_ADDRSTRLEN];

    signal(SIGINT, handler_kill);

	master = Open_clientfd(host, SLAVE_PORT);

    printf("[SLAVE] Connected to master server\n"); 

    while(1){
    	printf("[SLAVE] Waiting for client\n"); 

    	read(master, client_ip_string, INET_ADDRSTRLEN);

    	clientfd = Open_clientfd(client_ip_string, SLAVE_TO_CLIENT);
	    printf("[SLAVE] Connected to client (%s)\n", client_ip_string);

	    connected = 1;

    	while(connected){


    		// execution des requettes

    		if(attente_reponse(clientfd, LONG_TIMEOUT) > 0){

	    		n = read(clientfd, &int_cmd, sizeof(int));

	    		if(n > 0){

					switch(int_cmd){
						case EXIT:
							info = SLAVE_FINISHED;
				    		write(master, &info, sizeof(int));
				    		connected = 0;
				    		printf("[SLAVE] Finished with (%s)\n", client_ip_string);
				    		close(clientfd);
							break;
						case LS:
							argument[0] = malloc(sizeof(char)*5);
							strcpy(argument[0], "ls");
							argument[1] = NULL;
							/*sortie = dup(STDOUT_FILENO);
							dup2(clientfd, STDOUT_FILENO);
							execvp(argument[0], argument);
							printf("fini\n");
							dup2(sortie, STDOUT_FILENO);
							close(sortie);
							free(argument[0]);*/
							printf("[CLIENT] Not implemented yet\n");
							break;

						case GET : 
							info = OK;
							write(clientfd, &info, sizeof(int));




							nb_char = read(clientfd, filename, MAXLINE);
							filename[nb_char] = '\0';
							printf("[SLAVE] filename %s\n", filename);

							file = open(filename, O_RDONLY);

							if(file != -1){
								if(fstat(file, &file_stat) == 0){
									nb_octets = file_stat.st_size;
									printf("[SLAVE] file : %d bytes\n", nb_octets);
									write(clientfd, &nb_octets, sizeof(int));

									read(clientfd, &info, sizeof(int));
									if(info == OK){
										nb = 0;
										while(nb < nb_octets){


											nb_courant = read(file, buf, MAXLINE);
											nb += nb_courant;
											write(clientfd, buf, nb_courant);

											if(attente_reponse(clientfd, SHORT_TIMEOUT) > 0){
												read(clientfd, &info, sizeof(int));
												if(info != OK){
													printf("[SLAVE] Problem with client\n");
												}
											} else {
												printf("[SLAVE] Client has crashed during the transfert at %d bytes\n", nb-MAXLINE);
												connected = 0;
									    		info = CLIENT_CRASHED_DURING_TRANSFERT;
												write(master, &info, sizeof(int));
									    		close(clientfd);
									    		nb = nb_octets; // pour finir la boucle
											}

										}
										if(info != CLIENT_CRASHED_DURING_TRANSFERT){
											printf("[SLAVE] Transfert successfully done!\n");
										}


										
									} else {
										printf("[SLAVE] Error client crashed ?\n");
										close(clientfd);
										connected = 0;
									}								
								}

							} else {
								nb_octets = -1;
								write(clientfd, &nb_octets, sizeof(int));
								printf("[CLIENT] Error failed to open %s\n", filename);
							}



							break;
						default:
							break;
					}
	    		}
	    	} else { // fin if select

	    		printf("[SLAVE] Client has crashed\n");
	    		connected = 0;
	    		info = CLIENT_CRASHED;
				write(master, &info, sizeof(int));
	    		close(clientfd);
	    	}
    		

 

    	} /// while(connected)
    }

	exit(0);
}