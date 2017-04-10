
#include "stock_ip.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void print_list(node_t * head) {
    node_t * current = head;

    while (current != NULL) {
        printf("(%s, %d, %s) -> ", current->ip, current->nb_bytes, current->filename);
        current = current->next;
    }
    printf("\n");
}



void add(node_t ** head, char val_ip[INET_ADDRSTRLEN], int nb, char file[MAX_FILENAME]) {
    node_t * new_node;
    new_node = malloc(sizeof(node_t));

    new_node->nb_bytes = nb;
    strcpy(new_node->ip, val_ip);
    strcpy(new_node->filename, file);
    new_node->next = *head;
    *head = new_node;
}


void remove_first(node_t ** head) {
    node_t * next_node = NULL;

    if (*head == NULL) {
    	fprintf(stderr, "Error null pointer\n");
    }

    next_node = (*head)->next;
    free(*head);
    *head = next_node;

}

void remove_by_ip(node_t ** head, char val_ip[INET_ADDRSTRLEN]) {
    node_t *previous, *current;

    if (*head != NULL) {

	    if (!strcmp((*head)->ip, val_ip)) {
	        remove_first(head);
	    } else {

		    previous = current = (*head)->next;
		    while (current) {
		        if (!strcmp(current->ip, val_ip)) {
		            previous->next = current->next;
		            free(current);
		        }

		        previous = current;
		        current  = current->next;
		    }
	    }

        
    }
}

char * file_name(node_t ** head, char val_ip[INET_ADDRSTRLEN]) {
    node_t *current;

    if (*head != NULL) {

	    if (!strcmp((*head)->ip, val_ip)) {
	        return (*head)->filename;
	    } else {

		    current = (*head)->next;
		    while (current) {
		        if (!strcmp(current->ip, val_ip)) {
		            return current->filename;
		        }
		        current  = current->next;
		    }
		    return NULL;
	    }

        
    } else {
    	return NULL;
    }
}

int bytes(node_t ** head, char val_ip[INET_ADDRSTRLEN]) {
    node_t *current;

    if (*head != NULL) {

	    if (!strcmp((*head)->ip, val_ip)) {
	        return (*head)->nb_bytes;
	    } else {

		    current = (*head)->next;
		    while (current) {
		        if (!strcmp(current->ip, val_ip)) {
		            return current->nb_bytes;
		        }
		        current  = current->next;
		    }
		    return -1;
	    }

        
    } else {
    	return -1;
    }
}


int contain(node_t ** head, char val_ip[INET_ADDRSTRLEN]) {
    node_t *current;

    if (*head != NULL) {

	    if (!strcmp((*head)->ip, val_ip)) {
	        return 1;
	    } else {

		    current = (*head)->next;
		    while (current) {
		        if (!strcmp(current->ip, val_ip)) {
		            return 1;
		        }
		        current  = current->next;
		    }
		    return 0;
	    }

        
    } else {
    	return 0;
    }
}






