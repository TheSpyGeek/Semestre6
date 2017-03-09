#include "csapp.h"

#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define SHMSZ 27
#define C1 0
#define C2 1
#define T_pet 2




int main(){

	pid_t pid;


    int shmid;
    key_t key;
    char *shm;

    /* We'll name our shared memory segment"5678 */
    key = 5678;

    /* Create the segment. */
    if ((shmid = shmget(key, SHMSZ, IPC_CREAT | 0666)) < 0) {
        perror("shmget");
        exit(1);
    }

    /* Now we attach the segment to our data space. */
    if ((shm = shmat(shmid, NULL, 0)) == (char *) -1) {
        perror("shmat");
        exit(1);
    }


    shm[C1] = 0;
    shm[C2] = 0;
    shm[T_pet] = 1;


	if((pid = fork()) < 0){
		unix_error("Erreur fork");

	} else if(pid == 0){ // processus 1

		shm[C1] = 1;
		shm[T_pet] = 2;

		while(shm[C2] == 1 && shm[T_pet] == 2);

		printf("Debut section fils!\nOn attend un peu\n");
		sleep(1);
		printf("fin section fils\n");
		shm[C1] = 0;


	} else { // processus 2
		shm[C2] = 1;
		shm[T_pet] = 1;

		while(shm[C1] == 1 && shm[T_pet] == 1){
			
		}

		printf("Debut section père!\nOn attend un peu\n");
		sleep(1);
		printf("fin section père\n");
		shm[C2] = 0;
		waitpid(-1, NULL, 0);
	}

	exit(0);
}