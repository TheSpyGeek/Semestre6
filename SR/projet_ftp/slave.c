
#include "csapp.h"
#include "define.h"
#include "parse.h"


int connected = 0;
int master;
int clientfd;
char client_ip_string[INET_ADDRSTRLEN];
char filename[MAXLINE];
int nb_crash;


void sent_crash_to_master(char ip_string[], int nb_bytes, char filename[]);


void handler_kill(int sig){
	close(master);
	close(clientfd);
	exit(0);
}

void envoi_fichier_au_client(int file, int nb, int nb_octets, char filename[]);

int attente_reponse(int fd, int time);

void ls();


void reception_info_crash_from_master();
void envoi_info_crash_to_client();

int main(int argc, char **argv){


	char host[INET_ADDRSTRLEN];

	int int_cmd;
	int n, nb_octets, nb_char;
	int info;

    DIR* rep = NULL;
	struct dirent* fichierLu = NULL;
	char cwd[MAXLINE];
	int length;
	int taille;
	char buf[MAXLINE];
	char path[MAXLINE];

	int file;
	struct stat file_stat;

	if(argc < 2){
		strcpy(host, "localhost");
    } else {
    	strcpy(host, argv[1]);
    }


    signal(SIGINT, handler_kill);

	master = Open_clientfd(host, SLAVE_PORT);

    printf("[SLAVE] Connected to master server\n"); 

    while(1){
    	printf("[SLAVE] Waiting for client\n"); 

    	read(master, client_ip_string, INET_ADDRSTRLEN);

    	clientfd = Open_clientfd(client_ip_string, SLAVE_TO_CLIENT);
	    printf("[SLAVE] Connected to client (%s)\n", client_ip_string);

	    info = OK;
	    write(master, &info, sizeof(int));

	    if(attente_reponse(master, SHORT_TIMEOUT)){

	    	read(master, &info, sizeof(int));

	    	if(info == CLIENT_CRASHED){
	    		// printf("[SLAVE] Client crashed before\n");
				envoi_info(master, OK);

				reception_info_crash_from_master();

				printf("[SLAVE] Client (%s) crashed during transfert of %s at %d bytes\n", client_ip_string, filename, nb_crash);

				envoi_info(clientfd, CLIENT_CRASHED_DURING_TRANSFERT);

				envoi_info_crash_to_client();

	    	} else {
	    		envoi_info(master, OK);
	    		envoi_info(clientfd, DEFAULT);
	    	}



		    connected = 1;

	    	while(connected){


	    		// execution des requettes

	    		if(attente_reponse(clientfd, LONG_TIMEOUT) > 0){

		    		n = read(clientfd, &int_cmd, sizeof(int));

		    		if(n > 0){

						switch(int_cmd){
							case EXIT:
					    		envoi_info(master, SLAVE_FINISHED);
					    		connected = 0;
					    		printf("[SLAVE] Finished with (%s)\n", client_ip_string);
					    		close(clientfd);
								break;
							case LS:
								rep = NULL;
								fichierLu = NULL;
								printf("[SLAVE] Command ls\n");
							    getcwd(cwd, sizeof(cwd));
    							rep = opendir(cwd);

    							strcpy(buf, "");
    							taille = 0;

    							while ((fichierLu = readdir(rep)) != NULL){
    								if(strcmp(fichierLu->d_name, ".") && strcmp(fichierLu->d_name, "..")){
	    								length = strlen(fichierLu->d_name);
	    								taille += length +1;
	    								strcat(buf, fichierLu->d_name);
	    								strcat(buf, "\n");
    								}
    							}

    							write(clientfd, buf, taille);

								break;

							case PWD:
								printf("[SLAVE] Command pwd\n");
								char cwd[MAXLINE];
							    getcwd(cwd, sizeof(cwd));
							    write(clientfd, cwd, strlen(cwd));
								break;
							case CD:
								printf("[SLAVE] Command cd\n");
								envoi_info(clientfd, OK);

								n = read(clientfd, path, MAXLINE);
								path[n+1] = '\0';

								if(chdir(path) == 0){
									printf("[SLAVE] Current directory has changed\n");
								} else {
									printf("[SLAVE] Error with the path\n");
								}

								break;
							case GET :
								envoi_info(clientfd, OK);



								if(attente_reponse(clientfd, SHORT_TIMEOUT)){

									nb_char = read(clientfd, filename, MAXLINE);
									filename[nb_char] = '\0';
									printf("[SLAVE] filename %s\n", filename);

									file = open(filename, O_RDONLY);

									if(file != -1){

										if(fstat(file, &file_stat) == 0){
											nb_octets = file_stat.st_size;
											printf("[SLAVE] file : %d bytes\n", nb_octets);
											write(clientfd, &nb_octets, sizeof(int));

											if(attente_reponse(clientfd, SHORT_TIMEOUT)){

												read(clientfd, &info, sizeof(int));
												if(info == OK){
													
													
													envoi_fichier_au_client(file, 0, nb_octets, filename);


													
												} else {
													printf("[SLAVE] Info Error\n");
													close(clientfd);
													connected = 0;
												}
											} else {
												printf("[SLAVE] Client has crashed before start of transfert\n");
									    		connected = 0;
									    		info = CLIENT_CRASHED;
												write(master, &info, sizeof(int));
									    		close(clientfd);
											}						
										}

									} else {
										nb_octets = -1;
										write(clientfd, &nb_octets, sizeof(int));
										printf("[CLIENT] Error failed to open %s\n", filename);
									}
								} else {
									printf("[SLAVE] Client has crashed before send filename\n");
						    		connected = 0;
						    		info = CLIENT_CRASHED;
									write(master, &info, sizeof(int));
						    		close(clientfd);
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

	   	} ///attente de réponse

    }

	exit(0);
}


void sent_crash_to_master(char ip_string[], int nb_bytes, char filename[]){

	int info;

	if(attente_reponse(master, SHORT_TIMEOUT) > 0){
		read(master, &info, sizeof(int));
		
		if(info != OK){
			printf("[SLAVE] Problem with master 1 : error %d\n", info);
			exit(0);
		}

		write(master, ip_string, INET_ADDRSTRLEN);

		if(attente_reponse(master, SHORT_TIMEOUT) > 0){

			read(master, &info, sizeof(int));
			
			if(info != OK){
				printf("[SLAVE] Problem with master 2 : error %d\n", info);
				exit(0);
			}

			write(master, &nb_bytes, sizeof(int));

			if(attente_reponse(master, SHORT_TIMEOUT) > 0){

				read(master, &info, sizeof(int));

				if(info != OK){
					printf("[SLAVE] Problem with master 3 : error %d\n", info);
					exit(0);
				}
				
				write(master, filename, MAXLINE);

				printf("[SLAVE] All informations about crashed\n");
			} else {
				printf("[SLAVE] Master has crashed\n");
				close(master);
				exit(0);
			}
		} else {
			printf("[SLAVE] Master has crashed\n");
			close(master);
			exit(0);
		}
	} else {
		printf("[SLAVE] Master has crashed\n");
		close(master);
		exit(0);
	}
}



void envoi_fichier_au_client(int file, int nb, int nb_octets, char filename[]){

	int info;
	int nb_courant;
	char buf[MAXLINE];

	lseek(file, nb, SEEK_SET);

	while(nb < nb_octets){

		nb_courant = read(file, buf, MAXLINE);
		nb += nb_courant;
		write(clientfd, buf, nb_courant);

		if(attente_reponse(clientfd, SHORT_TIMEOUT) > 0){
			read(clientfd, &info, sizeof(int));
			if(info != OK){
				printf("[SLAVE] Problem with client nb = %d\n", nb);
			} else {

			}
		} else {
			if(nb-MAXLINE < 0){
				printf("[SLAVE] Client crashed \n");
				connected = 0;
				envoi_info(master, CLIENT_CRASHED);
				close(clientfd);
	    		nb = nb_octets+1; // pour finir la boucle
			} else {

				printf("[SLAVE] Client has crashed during the transfert at %d bytes\n", nb-MAXLINE);
				connected = 0;
				envoi_info(master, CLIENT_CRASHED_DURING_TRANSFERT);
	    		sent_crash_to_master(client_ip_string, nb-MAXLINE, filename);
	    		close(clientfd);
	    		nb = nb_octets+1; // pour finir la boucle
			}
		}

	}

	if(nb == nb_octets){
		printf("[SLAVE] Transfert successfully done!\n");
	} else {
		printf("[SLAVE] Problem during transfert\n");
	}

}

void reception_info_crash_from_master(){

	if(attente_reponse(master, SHORT_TIMEOUT)){

		read(master, filename, MAXLINE);

		envoi_info(master, OK);

		if(attente_reponse(master, SHORT_TIMEOUT)){

			read(master, &nb_crash, sizeof(int));
		} else {
			printf("[SLAVE] Master crashed\n");
		}


	} else {
		printf("[SLAVE] Master crashed\n");
	}



}


void envoi_info_crash_to_client(){
	int info, file, nb_octets;
	struct stat file_stat;


	if(attente_reponse(clientfd, SHORT_TIMEOUT)){
		read(clientfd, &info, sizeof(int));

		if(info == OK){

			write(clientfd, filename, strlen(filename));

			if(attente_reponse(clientfd, SHORT_TIMEOUT)){
				read(clientfd, &info, sizeof(int));

				if(info == OK){

					write(clientfd, &nb_crash, sizeof(int));

					if(attente_reponse(clientfd, SHORT_TIMEOUT)){
						read(clientfd, &info, sizeof(int));

						if(info == OK){

							file = open(filename, O_RDONLY);

							if(file != -1){

								if(fstat(file, &file_stat) == 0){
									nb_octets = file_stat.st_size;

									printf("[SLAVE] File %s has %d bytes\n", filename, nb_octets);

									write(clientfd, &nb_octets, sizeof(int));

									if(attente_reponse(clientfd, SHORT_TIMEOUT)){
										read(clientfd, &info, sizeof(int));

										if(info == OK){

											printf("[SLAVE] Transfert file %s starting at %d bytes\n", filename, nb_crash);

											envoi_fichier_au_client(file, nb_crash, nb_octets, filename);

										} else {
											printf("[SLAVE] Client problem\n");
											close(clientfd);
										}
									} else {
										printf("[SLAVE] Client has crashed before sending total bytes\n");
										close(clientfd);		
									}
								}
							} else {
								printf("[SLAVE] Problem during open file\n");
							}
						} else {
							printf("[SLAVE] Client problem\n");
							close(clientfd);
						}

					} else {
						printf("[SLAVE] Client has crashed\n");
						close(clientfd);		
					}

				} else {
					printf("[SLAVE] Client problem\n");
					close(clientfd);
				}
			} else {
				printf("[SLAVE] Client has crashed\n");
				close(clientfd);
			}
		} else {
			printf("[SLAVE] Client problem\n");
			close(clientfd);
		}					
	} else {
		printf("[SLAVE] Client has crashed\n");
		close(clientfd);
		
	}
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


 void ls(){


// while ((fichierLu = readdir(rep)) != NULL)
//     printf("Le fichier lu s'appelle '%s'\n", fichierLu->d_name);

 	
 }

