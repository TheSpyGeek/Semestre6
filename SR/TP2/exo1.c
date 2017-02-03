#include "csapp.h"

void handler1_1(int sig){
	return;
}

void handler1_2(int sig){
	printf("\nSignal %d\n", sig);
	return;
}

int main(int argc, char const *argv[])
{
	int i;

	if(argc < 2){
		printf("Argument : \n\t1.1 pour l\'exercice 1.1\n\t1.2 pour l\'exercice 1.2\n");
	} else {
		
		/// EXERCICE 1.1
		if(!strcmp(argv[1], "1.1") && argc == 3){
			Signal(SIGINT, handler1_1);
			// pause();
			i = sleep(atoi(argv[2]));
			printf("\nSeconde d\'arret : %d\n", atoi(argv[2])-i);

		/// EXERCICE 1.2
		} else if(!strcmp(argv[1], "1.2")){

			Signal(SIGINT, handler1_2); ///  CTRL-C
			//Signal(SIGKILL, handler1_2); //impossible
			//Signal(SIGSTOP, handler1_2); //impossible
			pause();

		}
			

		 
		
	}
	
	

	exit(0);
}