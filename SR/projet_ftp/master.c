
#include "csapp.h"
#include "define.h"
#include "stock_ip.h"

#include <sys/select.h>

int fd_slave[NB_ESCLAVE];
int busy[NB_ESCLAVE];
int tube[2];

fd_set readfd;
node_t * unfinished_transfert = NULL;



void handler_kill(int sig);

/// renvoie -1 si tous les esclaves sont occupés 
/// c'est a dire tous busy[i] == 1
/// sinon renvoie numero d'esclave non occupé
int slave_not_busy();

int fd_max(); // calcul de fd maximum dans le tableau des esclaves + tube[0]
void register_crash_transfert(int i);
int attente_reponse(int fd, int time);

void envoi_info_crash(int id, char ip[INET_ADDRSTRLEN]);

int main(int argc, char **argv){

    int listenfd;
    socklen_t clientlen;
    struct sockaddr_in clientaddr;
    int clientfd;
    int id_slave;
    int info;




    char client_hostname[MAX_NAME_LEN];
	char client_ip_string[INET_ADDRSTRLEN];

    signal(SIGINT, handler_kill);

    int nb_connected = 0;
    int nb_reponse;

    //// TIMEOUT
    struct timeval short_time;
    short_time.tv_sec = SHORT_TIMEOUT;
    short_time.tv_usec = 0;

    // connection des serveurs esclaves
    listenfd = Open_listenfd(SLAVE_PORT);

    while(nb_connected < NB_ESCLAVE){
    	fd_slave[nb_connected] = Accept(listenfd, (SA *)&clientaddr, &clientlen);
    	printf("[MASTER] Connection slave %d\n", nb_connected);
    	busy[nb_connected] = 0;
    	nb_connected++;
    }

    close(listenfd);

    printf("[MASTER] All slave connected\n");



	listenfd = Open_listenfd(CLIENT_PORT);


    pipe(tube);

    if(fork() == 0){ //// fils qui va gerer les informations qu'on lui envoie

    	close(tube[1]);

    	int max_fd = fd_max()+1;

    	while(1){

    		/// init select
    		FD_ZERO(&readfd);
	    	FD_SET(tube[0], &readfd);
	    	for(int i=0; i<NB_ESCLAVE; i++){
	    		FD_SET(fd_slave[i], &readfd);
	    	}

	    	/// attente de réponse
    		nb_reponse = select(max_fd, &readfd, 0, 0, &short_time);

    		if(nb_reponse > 0){

	    		for(int i=0; i<NB_ESCLAVE; i++){
	    			if(FD_ISSET(fd_slave[i], &readfd)){
	    				read(fd_slave[i], &info, sizeof(int));
	    				if(info == SLAVE_FINISHED){
	    					printf("[MASTER] Slave %d finished\n", i);
	    					busy[i] = 0;
	    				} else if(info == CLIENT_CRASHED){
	    					printf("[MASTER] Client of slave %d has crashed\n", i);
	    					busy[i] = 0;
	    				} else if(info == CLIENT_CRASHED_DURING_TRANSFERT){
	    					busy[i] = 0;
	    					printf("[MASTER] Client has crashed during the transfert\n");
	    					info = OK;
	    					write(fd_slave[i], &info, sizeof(int));

	    					//// RECUPERATION DES INFOS DU CRASH
	    					//// NOMBRE DE BYTES TRANSEFERES

	    					register_crash_transfert(i);
	    					
	    					// affichage des transferts non fini
	    					// print_list(unfinished_transfert);
	    				}
	    			}
	    		}

	    		if(FD_ISSET(tube[0], &readfd)){
	    			printf("[MASTER] Connection client\n");
	    			read(tube[0], client_ip_string, INET_ADDRSTRLEN);
	    			id_slave = slave_not_busy();

	    			if(id_slave >= 0){
	    				printf("[MASTER] IP sent to slave %d\n", id_slave);

	    				write(fd_slave[id_slave], client_ip_string, INET_ADDRSTRLEN);
	    				busy[id_slave] = 1;

	    				if(attente_reponse(fd_slave[id_slave], SHORT_TIMEOUT)){

	    					read(fd_slave[id_slave], &info, sizeof(int));
	    					if(info == OK){

	    						if(contain(&unfinished_transfert, client_ip_string)){
	    							info = CLIENT_CRASHED;
	    							write(fd_slave[id_slave], &info, sizeof(int));

	    							if(attente_reponse(fd_slave[id_slave], SHORT_TIMEOUT)){
	    								read(fd_slave[id_slave], &info, sizeof(int));
	    								if(info == OK){

	    									envoi_info_crash(id_slave, client_ip_string);
	    									remove_by_ip(&unfinished_transfert, client_ip_string);
	    								}
	    							} else {
				    					printf("[MASTER] Slave %d crashed\n", id_slave);
				    				}

	    								
	    						} else {
	    							envoi_info(fd_slave[id_slave], DEFAULT);
	    						}
	    					} else {
	    						printf("[MASTER] Problem with slave %d\n", id_slave);
	    					}

	    				} else {
	    					printf("[MASTER] Slave %d crashed\n", id_slave);
	    				}


	    			} else {
	    				printf("[MASTER] All slaves are busy\n");
	    			}
	    		}
    		}


    	}




    } else { /// pere qui reçoit les clients 

    	close(tube[0]);

		while(1){
			
			clientfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);
			clientlen = (socklen_t)sizeof(clientaddr);
		    Getnameinfo((SA *) &clientaddr, clientlen, client_hostname, MAX_NAME_LEN, 0, 0, 0);
	    	Inet_ntop(AF_INET, &clientaddr.sin_addr, client_ip_string, INET_ADDRSTRLEN);

		    write(tube[1], client_ip_string, INET_ADDRSTRLEN);

		    close(clientfd);
		}


    }

}

void envoi_info_crash(int id, char ip[INET_ADDRSTRLEN]){

	char *filename;
	int nb_bytes;
	int info;

	filename = file_name(&unfinished_transfert, ip);
	nb_bytes = bytes(&unfinished_transfert, ip);

	write(fd_slave[id], filename, strlen(filename));

	if(attente_reponse(fd_slave[id], SHORT_TIMEOUT)){

		read(fd_slave[id], &info, sizeof(int));

		if(info == OK){

			write(fd_slave[id], &nb_bytes, sizeof(int));
			printf("[MASTER] Info about crash sent to slave %d\n", id);

		} else {
			printf("[MASTER] Problem with Slave %d\n", id);
		}
	} else {
		printf("[MASTER] Slave %d has crashed\n", id);
	}



}


void register_crash_transfert(int i){

	char ip_client_crashed[INET_ADDRSTRLEN];
    int nb_bytes_crashed;
    char filename_client_crashed[MAXLINE];
    int nb_char;
    int info;

	if(attente_reponse(fd_slave[i], SHORT_TIMEOUT) > 0){
		nb_char = read(fd_slave[i], ip_client_crashed, INET_ADDRSTRLEN);
		ip_client_crashed[nb_char+1] = '\0';
		info = OK;
		write(fd_slave[i], &info, sizeof(int));

		if(attente_reponse(fd_slave[i], SHORT_TIMEOUT) > 0){
			nb_char = read(fd_slave[i], &nb_bytes_crashed, sizeof(int));
			info = OK;
			write(fd_slave[i], &info, sizeof(int));

			if(attente_reponse(fd_slave[i], SHORT_TIMEOUT) > 0){
				nb_char = read(fd_slave[i], filename_client_crashed, MAXLINE);
				filename_client_crashed[nb_char+1] = '\0';

				// ajout dans la liste
				add(&unfinished_transfert, ip_client_crashed, nb_bytes_crashed, filename_client_crashed);
				printf("[MASTER] Crash registered\n");
			} else {
				printf("[MASTER] Slave %d has crashed\n", i);
			}
		} else {
			printf("[MASTER] Slave %d has crashed\n", i);
		}
	} else {
		printf("[MASTER] Slave %d has crashed\n", i);
	}

}



//renvoie 1 si fd est occupé sinon 0
int slave_not_busy(){
	for(int i=0; i<NB_ESCLAVE; i++){
		if(!busy[i]){
			return i;
		}
	}
	return -1;
}


void handler_kill(int sig){
	printf("Shutdown server\n");
	for(int i=0; i<NB_ESCLAVE; i++){
		Close(fd_slave[i]);
	}
	exit(0);
}

int fd_max(){
	int fd = -1;

	for(int i=0; i<NB_ESCLAVE; i++){
		if(fd_slave[i] > fd){
			fd = fd_slave[i];
		}
	}
	if(tube[0] > fd){
		fd = tube[0];
	}

	return fd;
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
