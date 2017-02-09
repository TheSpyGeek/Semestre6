#include "csapp.h"

#define BUFFLENGTH 256


int main(){

	pid_t child;
	int fd[2];
	char entree[BUFFLENGTH];
	char sortie[BUFFLENGTH];

	int byteout = 0;


	pipe(fd);

	child = fork();

	if(child != 0){ // pere
		close(fd[0]);
		while((byteout = read(0, entree, BUFFLENGTH)) != 0){
			//byteout = read(0, entree, BUFFLENGTH);
			entree[byteout-1] = '\0';
			write(fd[1], entree, byteout);
			printf("[%d] fils je t\'envoie %s avec %d bytes\n", getpid(), entree, byteout-1);
			byteout = 0;
		}


	} else { //fils
		close(fd[1]);
		while(read(fd[0], sortie, BUFFLENGTH)){
			printf("[%d] j\'ai lu %s\n", getpid(), sortie);
		}
	}
	printf("[%d] fini\n", getpid());

	exit(0);


}