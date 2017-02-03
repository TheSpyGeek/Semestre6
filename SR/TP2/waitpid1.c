#include "csapp.h"
#define N 2

void Handler(int sig){

}

int main()
{
    int status, i;
    pid_t pid;

    for (i = 0; i < N; i++) {
        if ((pid = Fork()) == 0) {  /* child */

            char * adr = 0; // adresse au pif
            adr[0] = 15; //  on essaye d'ecrire

            exit(100+i);
        }
    }

    /* parent waits for all of its children to terminate */
    while ((pid = waitpid(-1, &status, 0)) > 0) {
        if (WIFEXITED(status)) {
            printf("child %ld terminated normally with exit status=%d\n",
                   (long)pid, WEXITSTATUS(status));
        } else {
            printf("child %ld terminated abnormally\n", (long)pid); 

            if(WIFSIGNALED(status)){ // si ça c'est terminé a cause d'un signal

                char *s = NULL;
                printf("Erreur signal : %d\n", WTERMSIG(status)); // on affiche le num du signal
                psignal(WTERMSIG(status), s); // on affiche a quoi ce signal correspond
            } 
        }
    }

    if (errno != ECHILD) {
        unix_error("waitpid error");
    }

    exit(0);
}
