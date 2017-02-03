#include "csapp.h"

int nbsig = 0;

void Handler(int sig){
	printf("bip\n");
	nbsig++;
	return;
}


int main(int argc, char const *argv[])
{

	Signal(SIGALRM, Handler);
	while(nbsig < 6){
		Alarm(1);
		sleep(1);
	}
	

	printf("bye\n");

	exit(0);
}