#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[], char *envp[]){
	
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
	wait(NULL);

	if (fork() == 0){ // child 2
		printf("Fils 2 PID : %d PPID : %d\n", getpid(), getppid());
		wait(NULL);
		exit(0);
	}
	wait(NULL);

	if (fork() == 0){ // child 2
		printf("Fils 3 PID : %d PPID : %d\n", getpid(), getppid());
		wait(NULL);
		exit(0);
	}

	wait(NULL);
	exit(0);
	return 0;
}