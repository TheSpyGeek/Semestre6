
#include <sys/types.h>
#include <stdio.h>

#define MAX_CMD 10
#define MAX_CHAR_CMD 1000

struct Jobs {
	pid_t Tab_pid[MAX_CMD];
	int background; // booleen pour savoir si les commandes sont au premiers plan
	int stopped; // si le processus a été stoppé
	int finished;
	char *cmd;
};

int length_seq(char ***seq);

void aff_jobs(struct Jobs);
