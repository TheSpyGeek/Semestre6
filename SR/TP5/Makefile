.PHONY: all, clean

# Disable implicit rules
.SUFFIXES:

CC = gcc
CFLAGS = -Wall -Werror
LDFLAGS =

# Note: -lnsl does not seem to work on Mac OS but will
# probably be necessary on Solaris for linking network-related functions 
#LIBS += -lsocket -lnsl -lrt
LIBS += -lpthread

INCLUDE = csapp.h tell_wait.h
OBJS = csapp.o tell_wait.o
INCLDIR = -I.

PROGS = shm_server shm_client mask peterson

all: $(PROGS)

%.o: %.c $(INCLUDE)
	$(CC) $(CFLAGS) $(INCLDIR) -c -o $@ $<

%: %.o $(OBJS)
	$(CC) -o $@ $(LDFLAGS) $^ $(LIBS)

clean:
	rm -f $(PROGS) *.o
