#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>

int main(){
	printf("PID : %d Parent : %d\n", getpid(), getppid());




	return 0;
}