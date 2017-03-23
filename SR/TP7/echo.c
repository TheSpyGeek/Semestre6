/*
 * echo - read and echo text lines until client closes connection
 */
#include "echo.h"

void echo(int connfd)
{
    size_t n, nb;
    char fich[MAXLINE];
    char buf[MAXLINE];
    rio_t rio;
    int fd;

    Rio_readinitb(&rio, connfd);

    nb = Rio_readlineb(&rio, fich, MAXLINE);
    if(nb > 1){
    	if(fich[nb-1] == '\n'){
    		fich[nb-1] = '\0';
    		nb--;
    	}
    }
    printf("Nom du fichier : %s\n", fich);

    fd = open(fich, O_RDONLY);

    if(fd != -1){
	    while ((n = read(fd, buf, MAXLINE)) != 0) {
	        printf("server received %u bytes\n", (unsigned int)n);
	        Rio_writen(connfd, buf, n);
	    }
    	
    }

    close(fd);

}

