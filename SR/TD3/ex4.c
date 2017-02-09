#include "csapp.h"
#include <stdio.h>

#define BUFSIZE 35

int main(){

	char bufin[BUFSIZE] = "rien";
	char bufout[BUFSIZE] = "Je suis ton pere";
	int bytesin, bytesout;
	pid_t childpid;
	int fd[2];
	int fd2[2];
	//int status;

	pipe(fd);
	pipe(fd2);
	//bytesin = strlen(bufin);
	childpid = fork();

	//while(1){
		if(childpid != 0){ // pere
			close(fd[0]);
			bytesout = write(fd[1], bufout, strlen(bufout)+1);
			printf("[%d] : J\'ecrit %d bytes, envoie %s à mon fils\n", getpid(), bytesout, bufout);

			//waitpid(-1, &status, 0);
			read(fd2[0], bufin, BUFSIZE);
			printf("[%d] J'ai lu %s de mon fils\n", getpid(), bufin);


		} else { // fils

			close(fd[1]);
			bytesin = read(fd[0], bufin, BUFSIZE);
			printf("[%d] : read %d bytes, my bufin is {%s}\n", getpid(), bytesin, bufin);

			strcpy(bufout,"NOOONNNNN");
			bytesout = write(fd2[1], bufout, strlen(bufout)+1);
			printf("[%d] : J\'ecrit %d bytes a mon père, envoie %s\n", getpid(), bytesout, bufout);


		}
		
	//}



	exit(0);

}