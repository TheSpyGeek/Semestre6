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
#include "jobs.h"

#define MAX_JOBS 20
#define MAX_CHAR_CMD_LINE 500



void ctrl_c(int sig);
void ctrl_z(int sig);
void stop_process_background();
int my_signal(int sig);
void handler_child(int sig);




 void close_pipe(int tubes[10][2], int seq_len){
 	int i;
 	for(i=0; i < seq_len; i ++){ //une fois après avoir écrit dans les pipes, il faut les fermer #prof
			close(tubes[i][0]);
			close(tubes[i][1]);
	}

 }


struct Jobs *Tab_Jobs;
int indice_jobs = -1;


struct cmdline *l;
int length;

int main(int argc, char *argv[], char *env[]){

	signal(SIGINT, ctrl_c);
	signal(SIGTSTP, ctrl_z);
	signal(SIGCHLD, handler_child);

	Tab_Jobs = malloc(sizeof(struct Jobs));


	while (1) {
		int i;
		int tubes[10][2]; 
		int in , out;
		pid_t pid;


		printf("shell> ");
		l = readcmd();

		//// on incremente le tableau de jobs
		if(l->seq[0]!=0){
			indice_jobs++;
			Tab_Jobs = realloc(Tab_Jobs, sizeof(struct Jobs)*(indice_jobs+1));
		}



		/* If input stream closed, normal termination */
		if (!l) {
			printf("exit\n");
			free(Tab_Jobs);
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
				free(Tab_Jobs);
				exit(0);
			}


			///// ECRITURE DE LA LIGNE DE COMMANDE DANS LA STRUCT JOBS

			if(i != 0){
				Tab_Jobs[indice_jobs].cmd = strcat(Tab_Jobs[indice_jobs].cmd, " | ");
			} else {
				Tab_Jobs[indice_jobs].cmd = malloc(sizeof(char) * MAX_CHAR_CMD_LINE);
				Tab_Jobs[indice_jobs].background = l->background;
				Tab_Jobs[indice_jobs].stopped = 0;
				Tab_Jobs[indice_jobs].finished = 0;
			}

			for(int j=0; cmd[j]!=0; j++){
				Tab_Jobs[indice_jobs].cmd = strcat(Tab_Jobs[indice_jobs].cmd, cmd[j]);
				Tab_Jobs[indice_jobs].cmd = strcat(Tab_Jobs[indice_jobs].cmd, " ");
			}
		

			// Creation d'un nouveau processus pour chaque commande

			if((pid = fork()) == 0){ // child


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
				execvp(cmd[0], cmd); // on execute		
				exit(0);
			}

			Tab_Jobs[indice_jobs].Tab_pid[i] = pid;
			
			close(tubes[i][1]);
			
		}

		Tab_Jobs[indice_jobs].Tab_pid[length] = 0; // pour savoir quand on s'arrete quand on parcourt
		close_pipe(tubes, length);


		//on attend tant que la commande ne s'est pas entièrement terminée
		while(!Tab_Jobs[indice_jobs].finished && !Tab_Jobs[indice_jobs].stopped && !Tab_Jobs[indice_jobs].background ){
			sleep(1);
		}		

		
	} //fin du while de boucle infini

}



void handler_child(int sig){
	int status;

	waitpid(-1, &status, WNOHANG|WUNTRACED);


	Tab_Jobs[indice_jobs].finished = 1; // on indique qu'il est fini pour sortir de la boucle de sleep

	if(WIFSTOPPED(status)){ // stoppé avec un ctrl z
		Tab_Jobs[indice_jobs].stopped = 1;
	} 

	return;
}


/// on enleve le traitement par default des signaux
void ctrl_c(int sig){
	return;
}

void ctrl_z(int sig){
	return;
}







