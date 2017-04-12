

#define GET 1
#define EXIT 0
#define LS 3
#define PWD 4
#define HOST 5
#define CD 6
#define RM 7
#define MKDIR 8
#define PUT 9
#define RMR 10
#define ERROR -1


char **split_in_words(char *line);
int cmd_to_int(char *cmd);
void no_endofline(char *cmd);