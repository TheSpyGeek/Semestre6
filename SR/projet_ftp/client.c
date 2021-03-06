

#include "csapp.h"
#include "define.h"
#include "parse.h"

int slave;

void handler_kill(int sig){
	int int_cmd = EXIT;
	write(slave, &int_cmd, sizeof(int));
	close(slave);
	printf("\n[CLIENT] Deconnection\n");
	// printf("CRASH\n");
	exit(0);

}


int attente_reponse(int fd, int time);

/// receptionne les données du crash précédent
void reception_donnees_crash();

// receptionne les données envoyé par le serveur esclave et les ajoutes dans le fichier file
void reception_fichier(int file, int nb, int nb_octets);

// envoie les données du fichier file au serveur esclave (pour le put) 
void envoi_fichier_a_esclave(int file, int nb, int nb_octets);

int main(int argc, char **argv){

	char host[INET_ADDRSTRLEN];
	int clientfd, listenfd;
	int int_cmd, info;
	char cmd[MAXLINE];
	char **cmd_parsed;
    socklen_t clientlen;
    struct sockaddr_in clientaddr;
    int nb_octets;
    int file;
    int n;
    char buf[MAXLINE];
    struct stat file_stat;

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
			reception_donnees_crash();
		} else if(info == DEFAULT){

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
					n = read(slave, buf, MAXLINE);
					write(STDOUT_FILENO, buf, n);
					break;

				case PWD:
					write(slave, &int_cmd, sizeof(int));
					n = read(slave, buf, MAXLINE);
					write(STDOUT_FILENO, buf, n);
					printf("\n");
					break;

				case ERROR :
					printf("[CLIENT] Error command unknown\n");
					break;
				case MKDIR:
				case PUT:
				case CD:
				case GET: 
					if(cmd_parsed[1] != 0){
						write(slave, &int_cmd, sizeof(int));

						if(attente_reponse(slave, SHORT_TIMEOUT) > 0){
							read(slave, &info, sizeof(int));

							if(info == OK){

								 //// POUR recuperer des fichiers
								if(int_cmd == GET){
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


									/// CREATION DE DOSSIER
								} else if(int_cmd == MKDIR){

									write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));
									// printf("[CLIENT] folder name (%s) sent\n", cmd_parsed[1]);

									// printf("[CLIENT] Folder %s created\n", cmd_parsed[1]);
								} else if(int_cmd == CD){

									write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));
								} else if(int_cmd == PUT){

									printf("passe ici\n");

									write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));

									if(attente_reponse(slave, SHORT_TIMEOUT)){
										read(slave, &info, sizeof(int));

										if(info == OK){

											file = open(cmd_parsed[1], O_RDONLY);

											if(file != -1){

												if(fstat(file, &file_stat) == 0){
													nb_octets = file_stat.st_size;
													printf("[SLAVE] file : %d bytes\n", nb_octets);

													write(slave, &nb_octets, sizeof(int));

													if(attente_reponse(slave, SHORT_TIMEOUT)){
														read(slave, &info, sizeof(int));

														if(info == OK){
															envoi_fichier_a_esclave(file, 0, nb_octets);
														} else {
															printf("[CLIENT] problem with slave\n");
															close(slave);
															exit(0);
														}

													} else {
														printf("[CLIENT] slave has crashed\n");
														close(slave);
														exit(0);
													}


												} else {
													printf("[CLIENT] Problem with fstat\n");
													nb_octets = -1;
													write(slave, &nb_octets, sizeof(int));
												}
											} else {
												nb_octets = -1;
												write(slave, &nb_octets, sizeof(int));
												printf("[CLIENT] Problem open file\n");
											}

										} else if(info == ERROR_OPEN){
											printf("[CLIENT] slave cannot open file\n");
										} else {
											printf("[CLIENT] Problem with slave\n");
										}


									} else {
										printf("[CLIENT] Slave a crash \n");
										close(slave);
										exit(0);
									}
								

								} else {
									printf("[CLIENT] mais mdr!\n");
									close(slave);
									exit(0);
								}
							} else if(info == NOT_IMPEMENTED){
								printf("[CLIENT] %s not implemented\n", cmd_parsed[0]);
							}
						} else {
							printf("[CLIENT] Slave has crashed\n");
							close(slave);
							exit(0);
						}
					} else {
						printf("[CLIENT] Error syntax : argument missing\n");
					}
					break;

				case RM:
					if(cmd_parsed[1] != 0){

						// RM -r 
						if(!strcmp(cmd_parsed[1], "-r") && cmd_parsed[2] !=0){
							int_cmd = RMR;
						// RM normal
						}

						write(slave, &int_cmd, sizeof(int));

						if(attente_reponse(slave, SHORT_TIMEOUT)){
							read(slave, &info, sizeof(int));
							if(info == OK){
								if(int_cmd == RMR){
									write(slave, cmd_parsed[2], strlen(cmd_parsed[2]));
									// printf("[CLIENT] folder name (%s) sent\n", cmd_parsed[2]);
								} else if(int_cmd == RM){
									write(slave, cmd_parsed[1], strlen(cmd_parsed[1]));
									// printf("[CLIENT] folder name (%s) sent\n", cmd_parsed[1]);
								} else {
									printf("[CLIENT] WTF!!!\n");
									close(slave);
									exit(0);
								}
							} else if(info == NOT_IMPEMENTED){
								printf("[CLIENT] rm not implemented yet\n");
							}

							// printf("[CLIENT] Delete successfully done\n");
	
						} else {
							printf("[CLIENT] Slave has crashed\n");
							close(slave);
							exit(0);
						}


					} else {
						printf("[CLIENT] Error rm syntax : rm <folder/file> or rm -r <folder/file>\n");
					}



					break;
				case HOST:
					if(cmd_parsed[1] != 0){
						if(fork() == 0){ // child
							char *args[] = {cmd_parsed[1], NULL};
							execvp(cmd_parsed[1], args);

						} else { // father
							wait(NULL);
						}
					} else {
						printf("[CLIENT] Error host syntax : host <command>\n");
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
	double duration;

	int nb_start = nb;

	lseek(file, nb, SEEK_SET);

	envoi_info(slave, OK);

	clock_t start, finish;
	start = clock();


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
	if(nb >= nb_octets){
		finish = clock();
		duration = (double)(finish - start) / CLOCKS_PER_SEC; 
		printf("[CLIENT] Transfert successfully done in %f sec at %f mo/s!\n", duration, (nb_octets - nb_start)/(duration*1000000));
		close(file);
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


void reception_donnees_crash(){

	int n, nb_octets, file;
	char filename[MAXLINE];
	int nb_bytes;

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
	} else {
		printf("[CLIENT] Slave has crashed\n");
		close(slave);
		exit(0);
	}
}


void envoi_fichier_a_esclave(int file, int nb, int nb_octets){

	int info;
	int nb_courant;
	char buf[MAXLINE];

	lseek(file, nb, SEEK_SET);

	while(nb < nb_octets){

		nb_courant = read(file, buf, MAXLINE);
		nb += nb_courant;
		write(slave, buf, nb_courant);

		if(attente_reponse(slave, SHORT_TIMEOUT) > 0){
			read(slave, &info, sizeof(int));
			if(info != OK){
				printf("[SLAVE] Problem with client nb = %d\n", nb);
			} else {

			}
		} else {
			printf("[CLIENT] Slave has crashed during trasnfert\n");
			close(slave);
			exit(0);
		}

	}

	if(nb == nb_octets){
		printf("[SLAVE] Transfert successfully done!\n");
	}

}