/*
 * echo - read and echo text lines until client closes connection
 */
#include "echo.h"


void echo(int connfd)
{
    size_t n, nb;
    char fich[MAXLINE];
    char buf[MAXBUF];
    rio_t rio;
    int fd;

    int info;
    /*
    
        info = 0 tous se passe bien
        info = 1 fichier non trouvé
        ajouter ici

    */


    Rio_readinitb(&rio, connfd);

    nb = Rio_readlineb(&rio, fich, MAXLINE);

    /*if(nb > 1){
    	if(fich[nb-1] == '\n'){
    		fich[nb-1] = '\0';
    		nb--;
    	}
    }*/
    printf("Nom du fichier : %s\n", fich);

    ///  Test si le fichier existe

    fd = open(fich, O_RDONLY);

    if(fd != -1){
        
        // envoyer une info pour dire qu'on a trouvé le fichier
        info = 0;
        printf("Envoie de l'info\n");
        Rio_writen(connfd, &info, sizeof(int));
        printf("Info envoyé\n");

	    while ((n = read(fd, buf, MAXBUF)) != 0) {
	        // printf("server received %u bytes\n", (unsigned int)n);
	        Rio_writen(connfd, buf, n);
	    }
    	
    } else {

    }

    close(fd);

}

