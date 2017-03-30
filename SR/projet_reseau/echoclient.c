/*
 * echoclient.c - An echo client
 */
#include "csapp.h"
#include "parse.h"
// #include "readcmd.h"

int clientfd;

void deco(int sig);

int main(int argc, char **argv)
{
    int port;
    char *host, buf[MAXLINE], cmd[MAXLINE];
    rio_t rio;
    int info;
    int fich;
    int n, nb_octets;

    char **cmd_parsed = 0;

    if (argc != 3) {
        fprintf(stderr, "usage: %s <host> <ftp name>\n", argv[0]);
        exit(0);
    }
    host = argv[1];

    if(!strcmp(argv[2], "my_ftp")){
        port = 2121;
    } else {
        fprintf(stderr, "Nom de ftp non reconnu\n");
        exit(0);
    }


    /*
     * Note that the 'host' can be a name or an IP address.
     * If necessary, Open_clientfd will perform the name resolution
     * to obtain the IP address.
     */
    clientfd = Open_clientfd(host, port);
    
    /*
     * At this stage, the connection is established between the client
     * and the server OS ... but it is possible that the server application
     * has not yet called "Accept" for this connection
     */
    printf("client connected to %s\n", argv[2]); 
    
    Rio_readinitb(&rio, clientfd);

    Signal(SIGINT, deco);


    while(1){

        printf("ftp > ");

        Fgets(cmd, MAXLINE, stdin);
        cmd[strlen(cmd)-1] = '\0';

        cmd_parsed = split_in_words(cmd);

        /// pour quitter le ftp
        if(cmd_parsed[0] != NULL && (!strcmp(cmd_parsed[0], "exit") || !strcmp(cmd_parsed[0], "quit"))){
            fprintf(stdout, "Deconnexion\n");
            exit(0);

        } else if(cmd_parsed[0] != NULL && !strcmp(cmd_parsed[0], "get")){
            if(cmd_parsed[1] == NULL){
                printf("Syntax : get <filename>\n");
            } else {

                printf("envoie du fichier %s\n", cmd_parsed[1]);

                //// recupération d'un fichier

                Rio_writen(clientfd, cmd_parsed[1], strlen(cmd_parsed[1]));

                printf("Pause\n");
                Rio_readnb(&rio, &info, sizeof(int));
                printf("Pause info == %d\n", info);


                if(info == 0){ // fichier trouvé

                    fich = open(cmd_parsed[1], O_WRONLY | O_CREAT, S_IWUSR | S_IRUSR | S_IRGRP  | S_IROTH);

                    nb_octets = 0;

                    while ((n = Rio_readlineb(&rio, buf, MAXLINE)) > 0) {
                        nb_octets += n;
                        write(fich, buf, n);
                    }

                    printf("Transfer successfully complete.\n");
                    printf("%d bytes transfered in 0.0 sec ça va vite!\n", nb_octets);

                } else {
                    fprintf(stderr, "Fichier %s non trouvé\n", cmd_parsed[1]);
                } 
            } 
        }

    }


    /*while (Fgets(buf, MAXLINE, stdin) == NULL) {}

    Rio_writen(clientfd, buf, strlen(buf));


    Rio_readnb(&rio, &info, sizeof(int));

    if(info == 0){ // fichier trouvé
        while (Rio_readlineb(&rio, buf, MAXLINE) > 0) {
            Fputs(buf, stdout);
        }
    } else {

    }*/

    

    printf("\n");
    Close(clientfd);
    exit(0);
}

void deco(int sig){

    printf("Deconnexion\n");
    exit(0);
}
