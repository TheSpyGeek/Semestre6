

#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>

#define MAX_FILENAME 8192

typedef struct node {
	char ip[INET_ADDRSTRLEN];
    int nb_bytes;
    char filename[MAX_FILENAME];
    struct node * next;
} node_t;


void print_list(node_t * head);
void add(node_t ** head, char val_ip[INET_ADDRSTRLEN], int nb, char file[MAX_FILENAME]);
void remove_first(node_t ** head);
void remove_by_ip(node_t ** head, char val_ip[INET_ADDRSTRLEN]);
char * file_name(node_t ** head, char val_ip[INET_ADDRSTRLEN]);
int bytes(node_t ** head, char val_ip[INET_ADDRSTRLEN]);

int contain(node_t ** head, char val_ip[INET_ADDRSTRLEN]);
