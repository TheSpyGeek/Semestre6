CC=gcc
CFLAGS=-Wall -g

all: shell

tst: tst.o readcmd.o jobs.o

shell: shell.o readcmd.o jobs.o


clean:
	rm -f shell shell.o readcmd.o tst tst.o
