#include "csapp.h"

#define INPUT_FILE  "./input2.bin"
#define SLEEP_TIME  1

/* ATTENTION : ne pas modifier cette procedure */
void sender(int fdout)
{
    int fdin;
    int n;
    char buf[MAXLINE];
    int s;
        
    fdin = Open(INPUT_FILE, O_RDONLY, 0);
    
    srand(21);  // initialisation du generateur de nombres pseudo-aleatoires 
    
    s = (rand()%9) + 1; // tirage pseudo-aleatoire
                        // d'une valeur entiere entre 1 et 9
                        // on peut modifier la sequence de tirages en jouant
                        // sur la valeur passee a srand
    while ( (n = Read(fdin, buf, s)) > 0) {
        Write(fdout, buf, n);
        sleep(SLEEP_TIME);
        s = (rand()%9) + 1;
    }
    Close(fdin);
    Close(fdout);
}


// 
// Tant qu'il y a des donnees disponibles dans le tube,
// lire une valeur (de type unsigned long long => 8 octets)
// et ecrire sur l'ecran la representation textuelle correspondante
// sur une nouvelle ligne (en utilisant printf - le format pour
// le type unsigned long long est %llu)
//
void receiver(int fdin)
{
    rio_t *rp;

    rio_readinitb(rp, fdin);

    int n;
    unsigned long long c;
    char buff[MAXLINE];

    while((n = (rio_readlineb(rp, buff, sizeof(char)*MAXLINE))) > 0){
        //printf("%llu\n", c);
        rio_writen(1, buff, n);
        printf("\n");
    }

    
    Close(fdin);
}



int main(int argc, char **argv)
{
    int pipe1[2];
    pid_t childpid;

    pipe(pipe1);    /* tube pere vers fils */

    if ( (childpid = Fork()) == 0) {    /* fils */
        
        Close(pipe1[1]);
        /* le fils lit sur la sortie du tube */
        receiver(pipe1[0]);
        exit(0);
    }
    
    /* pere */
    
    Close(pipe1[0]);    
    sender(pipe1[1]);

    Waitpid(childpid, NULL, 0);	        /* attendre fin fils */
    exit(0);
}
