
#define INPUT_FILE "./q2.txt"

#include "csapp.h"

int main(){

	int n;
	int fdin = Open(INPUT_FILE, O_RDONLY, 0);
	rio_t rp;
	char buff[MAXLINE];

    rio_readinitb(&rp, fdin);

    if((n = rio_readlineb(&rp, buff, MAXLINE)) > 0){
    	rio_writen(1, buff, n);
    }

    Rio_readnb(&rp, buff, 3);
    for(int i=0; i<3; i++){
    	putchar(buff[i]);
    }

    Rio_readnb(&rp, buff, 6);
    for(int i=0; i<6; i++){
    	putchar(buff[i]);
    }
    printf("\n");

    close(fdin);



    exit(0);


}