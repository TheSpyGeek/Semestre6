

#define GET 1
#define EXIT 0
#define LS 3
#define ERROR -1


char **split_in_words(char *line);
int cmd_to_int(char *cmd);
void no_endofline(char *cmd);