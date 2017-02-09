#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>



int main(int argc, char *argv[]){

	struct stat info;




	if(argc == 2){

		if(stat(argv[1], &info) == 0){

			printf("\n\tINFORMATIONS de %s\n\n", argv[1]);

			printf("Type : ");

			if(S_ISREG(info.st_mode)){
				printf("fichier \n");
			} else if(S_ISDIR(info.st_mode)){
				printf("repertoire\n");
			} else if(S_ISCHR(info.st_mode)){
				printf("periphérique en mode caractère\n");
			} else if(S_ISBLK(info.st_mode)){
				printf("periphérique en mode bloc\n");
			} else {
				printf("autres\n");
			}

			printf("Droits : -");
			if(info.st_mode & S_IRUSR){
				printf("r");
			} else {
				printf("-");
			}
			if(info.st_mode & S_IWUSR){
				printf("w");
			} else {
				printf("-");
			}if(info.st_mode & S_IXUSR){
				printf("x");
			} else {
				printf("-");
			}
			if(info.st_mode & S_IRGRP){
				printf("r");
			} else {
				printf("-");
			}
			if(info.st_mode & S_IWGRP){
				printf("w");
			} else {
				printf("-");
			}if(info.st_mode & S_IXGRP){
				printf("x");
			} else {
				printf("-");
			}
			if(info.st_mode & S_IROTH){
				printf("r");
			} else {
				printf("-");
			}
			if(info.st_mode & S_IWOTH){
				printf("w");
			} else {
				printf("-");
			}if(info.st_mode & S_IXOTH){
				printf("x");
			} else {
				printf("-");
			}
			//switch(info.st_mode & S_IRUSR)
			printf("\n");


			printf("Dernière modification : %s\n", ctime(&info.st_mtime));
			







			
		} else {
			printf("Erreur avec stat()\n");
		}
	} else {
		printf("Entrez un fchier en argument\n");
	}

	exit(0);
}