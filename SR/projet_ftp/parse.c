
#include "parse.h"
#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#include <errno.h>
#include <limits.h>
#include <string.h>

static void memory_error(void)
{
	errno = ENOMEM;
	perror(0);
	exit(1);
}


static void *xmalloc(size_t size) // alloue un pointeur d'espace size
{
	void *p = malloc(size);
	if (!p) memory_error();
	return p;
}


static void *xrealloc(void *ptr, size_t size)
{
	void *p = realloc(ptr, size);
	if (!p) memory_error();
	return p;
}




/* Split the string in words, according to the simple shell grammar. */
char **split_in_words(char *line)
{
	char *cur = line;
	char **tab = 0;
	size_t l = 0;
	char c;

	while ((c = *cur) != 0) {
		char *w = 0;
		char *start;
		switch (c) {
		case ' ':
		case '\t':
			/* Ignore any whitespace */
			cur++;
			break;
		case '<':
			w = "<";
			cur++;
			break;
		case '>':
			w = ">";
			cur++;
			break;
		case '|':
			w = "|";
			cur++;
			break;
		case '&':
			w = "&";
			cur++;
			break;
		default:
			/* Another word */
			start = cur;
			while (c) {
				c = *++cur;
				switch (c) {
				case 0:
				case ' ':
				case '\t':
				case '<':
				case '>':
				case '|':
					c = 0;
					break;
				default: ;
				}
			}
			w = xmalloc((cur - start + 1) * sizeof(char));
			strncpy(w, start, cur - start);
			w[cur - start] = 0;
		}
		if (w) {
			tab = xrealloc(tab, (l + 1) * sizeof(char *));
			tab[l++] = w;
		}
	}
	tab = xrealloc(tab, (l + 1) * sizeof(char *));
	tab[l++] = 0;
	return tab;
}

int cmd_to_int(char *cmd){

	if(!strcmp(cmd, "get")){
		return GET;
	} else if(!strcmp(cmd, "exit") || !strcmp(cmd, "quit") || !strcmp(cmd, "bye")){
		return EXIT;
	} else if(!strcmp(cmd, "ls")){
		return LS;
	} else if(!strcmp(cmd, "pwd")){
		return PWD;
	} else if(!strcmp(cmd, "host")){
		return HOST;
	} else if(!strcmp(cmd, "cd")){
		return CD;
	} else if(!strcmp(cmd, "rm")){
		return RM;
	} else if(!strcmp(cmd, "mkdir")){
		return MKDIR;
	} else if(!strcmp(cmd, "put")){
		return PUT;
	} else {
		return ERROR;
	}
}

void no_endofline(char *cmd){
	int n = strlen(cmd);

	if(n > 0){
		cmd[n-1] = '\0';
	}
}



