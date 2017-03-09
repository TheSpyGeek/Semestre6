void handler_sigint(int sig)
{
    printf("Caught SIGINT\n");
    return;
}

void handler_sigusr1(int sig)
{
    printf("Caught SIGUSR1\n");
    return;
}

int main()
{
    sigset_t s1;
    sigset_t s2;
    int i;

    printf("NB_SIG=%d, SIGRT_ON=%d\n", NB_SIG, SIGRT_ON);

    Signal(SIGINT, handler_sigint);
    Signal(SIGUSR1, handler_sigusr1);

    Sigemptyset(&s1);
    Sigaddset(&s1, SIGINT);
    Sigaddset(&s1, SIGUSR1);
    Sigprocmask(SIG_BLOCK, &s1, NULL);
    printf("Signals SIGINT (%d) and SIGUSR1 (%d) should now be blocked\n",
           SIGINT, SIGUSR1);

    printf("Sending SIGINT to myself\n");
    Kill(getpid(), SIGINT);
    printf("Sending SIGUSR1 to myself\n");
    Kill(getpid(), SIGUSR1);

    Sigemptyset(&s2);
    sigpending(&s2);
    for (i=1; i < NB_SIG; i++) {
        if (Sigismember(&s2, i)) {
            printf("Signal %d is currently pending\n", i);
        }
    }

    printf("Unblocking SIGINT and SIGUSR1\n");
    Sigprocmask(SIG_UNBLOCK, &s1, NULL);
    sleep(1);

    exit(0);
}
