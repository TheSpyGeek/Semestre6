
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[], char *envp[]){
	
	int i;

	while(envp[i] != NULL){
		printf("%s\n", envp[i]);
		i++;
	}




	return 0;
}