#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <stdio.h>
#include <stdlib.h>

#define STDIN 0
#define STDOUT 1
#define STDERR 2

#define TAILLE_BUFF 64

int main(int argc, char *argv[]){


	if(argc == 2){

		int in = open(argv[1], O_RDONLY);

		if(in){
			char  *buff = malloc(TAILLE_BUFF); // void buff
			int size;

			while(size = read(in, buff, TAILLE_BUFF)){ /// tant qu'on peut lire
				write(STDOUT, buff, size); // on ecrit
			}

			close(in);
			free(buff);

		} else {
			fprintf(stderr, "Erreur ouverture fichier\n");
			exit(1);
		}

		exit(0);
	} else {
		printf("Entrez le fichier en argument de programme\n");
		exit(1);
	}

}

