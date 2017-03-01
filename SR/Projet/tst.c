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

void ctrl_c(int sig);
void ctrl_z(int sig);
void stop_process_foreground();
int my_signal(int sig);

 void close_pipe(int tubes[10][2], int seq_len){
 	int i;
 	for(i=0; i < seq_len; i ++){ //une fois après avoir écrit dans les pipes, il faut les fermer #prof
			close(tubes[i][0]);
			close(tubes[i][1]);
	}

 }

 int length_seq(char ***seq){
 	int i;
 	for (i=0; seq[i]!=0; i++);
 	return i;
 }


struct cmdline *l;

int main(int argc, char *argv[], char *env[]){

	signal(SIGINT, ctrl_c);
	signal(SIGTSTP, ctrl_z);

	while (1) {
		int i, length;
		int tubes[10][2]; // on peut faire en double pointeur avec malloc si tu préfères <3
		int in , out;
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

		if(l->out != NULL){ // creation du filedescriptor des sorties
			out = open(l->out, O_WRONLY | O_CREAT, S_IWUSR | S_IRUSR | S_IRGRP  | S_IROTH);
		} 

		if(l->in != NULL){ // creation du filedescriptor des entrées 
			in = open(l->in, O_RDONLY);
		}

		length = length_seq(l->seq);


		for (i=0; i<length; i++) {// boucle ouverture pipe
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

				////  SI Y A UN PIPE
				if(length > 1){
					if(i == 0){ //cas premier cmd du pipe
						dup2(tubes[i][1], STDOUT_FILENO); 
						if(l->in != NULL){
							dup2(in, STDIN_FILENO);
							close(in);
						}

					}

					else if (i == length - 1){ //cas dernière cmd
						dup2(tubes[i-1][0], STDIN_FILENO); 
						if(l->out != NULL){ // redirection des sorties
							dup2(out, STDOUT_FILENO); // 1 = stdout
							close(out);
						} 

					}

					else{ //cas général
						dup2(tubes[i-1][0], STDIN_FILENO);
						dup2(tubes[i][1], STDOUT_FILENO);	
					}



				} // SI Y A PAS BESOIN DE PIPE


				else {
					if(l->out != NULL){ // redirection des sorties
						dup2(out, STDOUT_FILENO); // 1 = stdout
						close(out);
					} 

					if(l->in != NULL){ //redirection des entées
						dup2(in, STDIN_FILENO);
						close(in);
					}
				}

				close_pipe(tubes, length);
				execvp(cmd[0], cmd);
				exit(0); 


			}

		

			
			close(tubes[i][1]);
			
		} //fin du for
		// on est cons, faut attendre tout les fils à chaque itération

		close_pipe(tubes, length);

		if(!l->background){ // si ce n'est pas en background
			for(i=0;l->seq[i]!=0; i++){
				waitpid(-1, &status, 0);
			}
		}

		
	} //fin du while de boucle infini

}


void ctrl_c(int sig){
	/*int i;
	for(i=0;l->seq[i]!=0; i++){
		kill(getpid(), SIGINT);
	}*/
	return;
}

void ctrl_z(int sig){
	/*int i;
	for(i=0;l->seq[i]!=0; i++){
		kill(, SIGTSTP);
	}*/

	return;

}



