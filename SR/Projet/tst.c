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

 void close_pipe(int tubes[10][2], int seq_len){
 	for(int k; k < seq_len; k ++){ //une fois après avoir écrit dans les pipes, il faut les fermer #prof
			close(tubes[k][0]);
			close(tubes[k][1]);
	}

 }

 int lenght_seq(char ***seq){
 	int i;
 	for (i=0; seq[i]!=0; i++);
 	return i;
 }

int main(int argc, char *argv[], char *env[])
{
	while (1) {
		struct cmdline *l;
		int i, j, lenght;
		int status;
		int tubes[10][2]; // on peut faire en double pointeur avec malloc si tu préfères <3
		int in , out;


		printf("shell> ");
		l = readcmd();

		lenght = lenght_seq(l->seq);

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

		if (l->in) {
			printf("in: %s\n", l->in);
			in = open(l->in, O_RDWR | O_TRUNC | O_CREAT, S_IWUSR | S_IRUSR); //on a besoin d'pen qu'une fois
		}
		if (l->out){
			printf("out: %s\n", l->out);
			out = open(l->out, O_RDWR | O_TRUNC | O_CREAT, S_IWUSR | S_IRUSR);


		} 
		for (i=0; i<lenght; i++) {// boucle ouverture pipe
			pipe(tubes[i]);
		}

		/* Display each command of the pipe */
		for (i=0; l->seq[i]!=0; i++) { 
			char **cmd = l->seq[i];
		

			////exit
			if(!strcmp(cmd[0], "exit")){
				printf("\n");
				exit(0);
			}


			if(fork() == 0){ // child

				if(lenght > 1){
					if(i == 0){ //cas premier cmd du pipe
						dup2(tubes[i][1], STDOUT_FILENO); 
						if(l->in != NULL){
							dup2(in, STDIN_FILENO);
							close(in);
						}

					}

					else if (i == lenght - 1){ //cas dernière cmd
						dup2(tubes[i-1][0], STDIN_FILENO); //STDIN_FILENO == 0, mais ça bug si on met 0
						if(l->out != NULL){ // redirection des sorties
							dup2(out, STDOUT_FILENO); // 1 = stdout
							close(out);
						} 

					}

					else{ //cas général
						dup2(tubes[i-1][0], STDIN_FILENO);
						dup2(tubes[i][1], STDOUT_FILENO);	
					}

				}
				else {
					if(l->out != NULL){ // redirection des sorties
						dup2(out, STDOUT_FILENO); // 1 = stdout
						close(out);
					} 

					if(l->in != NULL){
						dup2(in, STDIN_FILENO);
						close(in);
					}
				}

				close_pipe(tubes, lenght);
				execvp(cmd[0], cmd);
				exit(0); 


			}

			
			close(tubes[i][1]);
			
		} //fin du for
		// on est cons, faut attendre tout les fils à chaque itération

		close_pipe(tubes, lenght);
		for(int k;l->seq[k]!=0; k++){
				waitpid(-1, &status, 0);
		}
		
	} //fin du while

}
