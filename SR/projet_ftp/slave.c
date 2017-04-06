
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

int main(int argc, char **argv){


	char host[] = "thespygeek";

	int connected = 0;
	int int_cmd;
	int n;
	int info;
	char filename[MAXLINE];

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
						printf("[SLAVE] ls not implemented yet\n");
						break;

					case GET : 
						info = OK;
						write(clientfd, &info, sizeof(int));
						read(clientfd, filename, MAXLINE);
						printf("[SLAVE] filename %s\n", filename);
						break;
					default:
						break;
				}
    		}

    		// info = SLAVE_FINISHED;
    		// write(master, &info, sizeof(int));
    		// connected = 0;
    		// close(clientfd);
    		// printf("[SLAVE] Finished\n");

    	}
    }

	exit(0);
}