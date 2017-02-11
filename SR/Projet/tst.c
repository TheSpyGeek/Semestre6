/*
 * Copyright (C) 2002, Simon Nieuviarts
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <setjmp.h>
#include <signal.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <errno.h>
#include <math.h>
#include <pthread.h>
#include <semaphore.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <inttypes.h>	// added to support C99 types and related macros
#include <strings.h>	// added to support solaris

#include "readcmd.h"

int main(int argc, char *argv[], char *env[])
{
	while (1) {
		struct cmdline *l;
		int i, j;
		int status;


		printf("shell> ");
		l = readcmd();

		/* If input stream closed, normal termination */
		if (!l) {
			printf("exit\n");
			exit(0);
		}

		if (l->err) {
			/* Syntax error, read another command */
			printf("error: %s\n", l->err);
			continue;
		}

		if (l->in) printf("in: %s\n", l->in);
		if (l->out) printf("out: %s\n", l->out);

		/* Display each command of the pipe */
		for (i=0; l->seq[i]!=0; i++) { // di
			char **cmd = l->seq[i];
			printf("seq[%d]: ", i);
			for (j=0; cmd[j]!=0; j++){
				printf("%s ", cmd[j]);

				

			}

			////exit
			if(!strcmp(cmd[0], "exit")){
				printf("\n");
				exit(0);
			}

			if(fork() == 0){ // child

				/*if(l->in != NULL){ // redirection des entrées

				} else {

				}*/

				if(l->out != NULL){ // redirection des sorties
					int out = open(l->out, O_RDWR | O_TRUNC | O_CREAT, S_IWUSR | S_IRUSR);
					dup2(out, 1); // 1 = stdout
					close(out);
				} 

				if(l->in != NULL){
					int in = open(l->in, O_RDWR | O_TRUNC | O_CREAT, S_IWUSR | S_IRUSR);
					dup2(in, 0);
					close(in);
				}
				


				execvp(cmd[0], cmd);
				exit(0); 


			}

			waitpid(-1, &status, 0);
			printf("\n");
		}

		// execution avec pipe

		//for(int i=0; l->seq[i]!=0; i++){

		//}
		
		



	}

}
