/*
 * echoserveri.c - An iterative echo server
 */

#include "csapp.h"
#include "echo.h"

#define MAX_NAME_LEN 256

#define NPROC 3

pid_t tab[NPROC];


/* 
 * Note that this code only works with IPv4 addresses
 * (IPv6 is not supported)
 */

void handler_kill(int sig);
void handler_fork(int sig);

int main(int argc, char **argv)
{
	pid_t pid;
	int status;
    int listenfd, connfd, port;
    socklen_t clientlen;
    struct sockaddr_in clientaddr;
    char client_ip_string[INET_ADDRSTRLEN];
    char client_hostname[MAX_NAME_LEN];

    signal(SIGINT, handler_kill);
    
    if (argc != 2) {
        fprintf(stderr, "usage: %s <port>\n", argv[0]);
        exit(0);
    }
    port = atoi(argv[1]);
    
    clientlen = (socklen_t)sizeof(clientaddr);

    listenfd = Open_listenfd(port);


    for(int i=0; i<NPROC; i++){
    	pid = fork();
    	if(pid == 0){ // fils
    		printf("Creation du fils %d\n", getpid());
    		tab[i] = getpid();
    	} else { // pere
    		wait(NULL);
    	}
    }
    if(pid == 0){
	    while (1) {
	        
	        connfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);

        	/* determine the name of the client */
	        Getnameinfo((SA *) &clientaddr, clientlen, client_hostname, MAX_NAME_LEN, 0, 0, 0);
	        
	        /* determine the textual representation of the client's IP address */
	        Inet_ntop(AF_INET, &clientaddr.sin_addr, client_ip_string, INET_ADDRSTRLEN);
	        
	        printf("server connected to %s (%s)\n", client_hostname, client_ip_string);

	        echo(connfd);
		    Close(connfd);
	    }
    	
    } else {
    	for(int i=0; i<NPROC; i++){
    		waitpid(-1, &status, 0);
    	}
    }
}

void handler_fork(int sig){
	int pid, status;

	if((pid = wait(&status)) == -1){ // erreur pendant le wait

		fprintf(stderr, "Error wait !!!\n");
	} else {
		fprintf(stdout, "Deconnexion %d\n", pid);
	}
	// sinon tous se passe bien le zombie est bien attendu
	return;
}

void handler_kill(int sig){

	for(int i=0; i<NPROC; i++){
		kill(tab[i], SIGINT);
	}
	exit(0);
	return;
}