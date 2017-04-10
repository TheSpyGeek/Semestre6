


#define NB_ESCLAVE 1
#define MAX_NAME_LEN 256

#define SLAVE_TO_CLIENT 2121
#define SLAVE_PORT 2122
#define CLIENT_PORT 2123


// CMD CODE

#define CMD_DECO 0
#define CMD_GET 1


/// REQUEST MASTER


#define SLAVE_FINISHED 0
#define CLIENT_CRASHED 4
#define CLIENT_CRASHED_DURING_TRANSFERT 2
#define DEFAULT 3

#define NOT_BUSY 0
#define BUSY 1

#define OK 1
#define ERROR_OPEN 2

#define SHORT_TIMEOUT 3
#define LONG_TIMEOUT 90


void envoi_info(int fd, int info){
	int cpy = info;
	write(fd, &cpy, sizeof(int));
}



