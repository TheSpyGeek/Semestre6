#include "csapp.h"

void handler(int sig){
	//printf("Caught SIGINT\n");
	//exit(0);
	return;
}

int main(int argc, char const *argv[])
{
	int i;
	if(argc > 1){
		Signal(SIGINT, handler);
		// pause();
		i = sleep(atoi(argv[1]));
		printf("\nSeconde d\'arret : %d\n", atoi(argv[1])-i);
	}
	
	printf("passe\n");

	exit(0);
}