
#include "jobs.h"


int length_seq(char ***seq){
	int i;
	for (i=0; seq[i]!=0; i++);
	return i;
}



void aff_jobs(struct Jobs j){

	printf("\n\nLigne de commande : %s\n", j.cmd);
	printf("Foreground : %d\n", j.background);
	printf("Cmd stopped : %d\n", j.stopped);
	printf("PID ::::\n");

	for(int i=0; j.Tab_pid[i] != 0; i++){
		printf("\t%d : %d\n", i, j.Tab_pid[i]);
	}
	printf("\n\n");
}
