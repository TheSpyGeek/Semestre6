

#include "csapp.h"
#include "tell_wait.h"

void slow_write(char *str){

	char *ptr;
	char c;
	int i;

	ptr = str;
	setbuf(stdout, NULL); // unbuffered mode
	while(*ptr != 0){
		c = *ptr;
		putc((int)c, stdout); // print
		ptr++;
		for(i=0;i<50000;i++);
	}
}

int main(){
	pid_t pid;

	init_synchro();

	if((pid = fork()) < 0){
		unix_error("fork error");
	} else if(pid == 0){ // child 
		wait_parent();
		slow_write("output from child\n");
	} else { // parent
		slow_write("output from parent\n");
		tell_child(pid);
		//wait(NULL);
	}

	exit(0);
}