

#include "csapp.h"
#include "tell_wait.h"


int main(){
	pid_t pid;

	if((pid = fork()) < 0){
		unix_error("fork error");
	} else if(pid == 0){ // child
		tell_parent();
		step1(A2);
		tell_parent();
		wait_parent();
		step2(B2);
		tell_parent();
		wait_parent();
		step3(C2);
		tell_parent();
		wait_parent();
	} else { /// pere

		step1(A1);
		wait_child();
		merge(A1, A2);


		step2(B1);
		tell_child(pid);
		wait_child();
		merge(B1, B2);

		step3(C1);
		tell_child(pid);
		wait_child();
		merge(C1, C2);
	}
}