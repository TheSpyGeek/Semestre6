/* $begin signal1 */
#include "csapp.h"

void handler1(int sig)
{
    pid_t pid;

    if ((pid = waitpid(-1, NULL, WNOHANG)) < 0)
        unix_error("waitpid error");

    printf("Handler reaped child %d\n", (int)pid);
    Sleep(2); /* the only purpose of this line is to exacerbate the problems */
    return;
}

int main()
{
    int i;
    //int n;
    //char buf[MAXBUF];

    Signal(SIGCHLD, handler1);

    /* parent creates children */
    for (i = 0; i < 5; i++) {
        if (Fork() == 0) {
            printf("Hello from child %d\n", (int)getpid());
            Sleep(i+1);
            exit(0);
        }
    }

    printf("Parent entering loop\n");    
    /* in a realistic program, this empty loop would be replaced
       by something useful */
    
    while (1) {
        pause();
    }

    exit(0);
}
/* $end signal1 */
