
#include "csapp.h"

int main(int argc, char *argv[], char *envp[]){
	
	int i;

	if(argc > 1){
		if(!strcmp(argv[1], "3.1")){ // exercice 3.1
			printf("Pere PID : %d PPID : %d\n", getpid(), getppid());

			if(fork() == 0){ // child 1
				printf("Fils 1 PID : %d PPID : %d\n", getpid(), getppid());

				if(fork() == 0){ // little child 1
					printf("Petit fils 1 PID : %d PPID : %d\n", getpid(), getppid());
					wait(NULL);
					exit(0);
				}

				wait(NULL);

				if(fork() == 0){ // little child 2
					printf("Petit fils 2 PID : %d PPID : %d\n", getpid(), getppid());
					wait(NULL);
					exit(0);
				}

				wait(NULL);
				exit(0);
			}

			if (fork() == 0){ // child 2
				printf("Fils 2 PID : %d PPID : %d\n", getpid(), getppid());
				wait(NULL);
				exit(0);
			}

			if (fork() == 0){ // child 2
				printf("Fils 3 PID : %d PPID : %d\n", getpid(), getppid());
				wait(NULL);
				exit(0);
			}


			exit(0);

			/*
			Ordre apparition :
			Pere
			Fils 1
			Fils 2
			Petit fils 1
			Fils 3
			Petit fils 2

			*/

		} else if(!strcmp(argv[1], "3.3")){

			if(argc > 3){
				if(!strcmp(argv[2],"arbre")){ // sous forme d'arbre
				/*
							 |------exit
							 |
						|----| --- exit
						|
					----|----exit

				*/
					printf("Sous forme arbre\n");
					printf("Pere PID : %d PPID : %d\n", getpid(), getppid());
					for(i=0; i<atoi(argv[3]); i++){
						if(fork() > 0){ // pere
							printf("Fils PID : %d PPID : %d\n", getpid(), getppid());
							wait(NULL);
							exit(0);
						}
					}
					wait(NULL);
					exit(0);
					

				} else if(!strcmp(argv[2],"chaine")){ // sous forme de chaine
				/*

					|-----exit	|--------exit	|------exit
					|			|				|
				----|-----------|---------------|---------------exit

				*/
					printf("Sous forme chaine\n");
					printf("Pere PID : %d PPID : %d\n", getpid(), getppid());
					for(i=0;i<atoi(argv[3]); i++){
						if(fork() == 0){ // child
							printf("Fils PID : %d PPID : %d\n", getpid(), getppid());
							exit(0);
						}
						wait(NULL); // il faut que le pere attende
						
					}
					wait(NULL);
					exit(0);


				} else {
					printf("Mauvais type de construction\n");
				}
			} else {
				printf("Exercice  3.3 Mauvais nombre d\'arguments\n");
			}
		} else {
			printf("Mauvais non d\'exo\n");
		}
	} else {
		printf("Mauvais nombre d\'arguments\n");
	}
	
	return 0;
}

