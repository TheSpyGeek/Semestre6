
#include "define.h"
#include "csapp.h"

#include <sys/select.h>

int fd_slave[NB_ESCLAVE];
int busy[NB_ESCLAVE];
int tube[2];

fd_set readfd;




void handler_kill(int sig);

/// renvoie -1 si tous les esclaves sont occupés 
/// c'est a dire tous busy[i] == 1
/// sinon renvoie numero d'esclave non occupé
int slave_not_busy();

int fd_max(); // calcul de fd maximum dans le tableau des esclaves + tube[0]

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

    /*while(1){
    	id_slave = slave_not_busy();
    	printf("Slave = %d\n", id_slave);
    	if(id_slave != -1){


	    	// clientlen = (socklen_t)sizeof(clientaddr);
	    	// printf("Client : hostname : %s IP : (%s)\n", client_hostname, client_ip_string);


	    	printf("Info sent\n");
	    	info = OPEN_CLIENT;
	    	write(fd_slave[id_slave], &info, sizeof(int));

	    	read(fd_slave[id_slave], &info, sizeof(int));

	    	if(info == OK){
	    		write(fd_slave[id_slave], &clientaddr, clientlen);	
	    	} else {
	    		printf("Erreur connection du slave %d\n", id_slave);
	    	}
    		
    	}

    }*/



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

