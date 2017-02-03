#include "csapp.h"
int counter = 0;
void handler(int sig)
{
    counter++;
    sleep(1); // si on enleve ce sleep on peut avoir un nombre différent de fils
    return;
}

int main()
{
    int i;
    Signal(SIGUSR2, handler);

    if (Fork() == 0) {  /* child */
        for (i = 0; i < 5; i++) {
            Kill(getppid(), SIGUSR2);
            printf("sent SIGUSR2 to parent\n");
        }
        exit(0);
    }

    Wait(NULL);
    printf("counter=%d\n", counter);
    exit(0);
}
