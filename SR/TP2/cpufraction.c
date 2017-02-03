#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/times.h>

// This is the function for which we want to measure the execution time
void function_to_time(void) {
    // TO BE COMPLETED  
}

int main(void) {
    double clockticks, cticks, cticks_u, cticks_s;
    clock_t tc;
    unsigned long long tcend, tcstart, wall_clock_time;
    struct tms tmend, tmstart;

    if ((clockticks = (double) sysconf(_SC_CLK_TCK)) == -1) {
        perror("Failed to determine clock ticks per second");
        return 1;
    }
    printf("The number of ticks per second is %f\n", clockticks);
    if (clockticks == 0) {
        fprintf(stderr, "The number of ticks per second is invalid\n");
        return 1;
    }
    
    // Before calling function_to_time,
    // we read (and store) the following data:
    // - in tcstart: the current number of clock ticks that have elapsed
    //               since an arbitrary reference point in the past
    // - in tmstart.tms_utime: the time (in number of clock ticks) that has
    //                         been spent executing the calling process
    //                         IN USER MODE since its start.
    // - in tmstart.tms_stime: the time (in number of clock ticks) that has
    //                         been spent executing the calling process
    //                         IN SUPERVISOR MODE (mostly system calls)
    //                         since its start.
    if ((tc = times(&tmstart)) == (clock_t)-1) {
        perror("Failed to get start time");
        return 1;
    }
    tcstart = (unsigned long long)tc;
    
    function_to_time();
    
    // After calling function_to_time,
    // we read (and store) the following data:
    // - in tcend: the current number of clock ticks that have elapsed
    //               since an arbitrary reference point in the past
    //               (this reference point is the same as the one of tcstart)
    // - in tmend.tms_utime: the time (in number of clock ticks) that has
    //                       been spent executing the calling process
    //                       IN USER MODE since its start.
    // - in tmend.tms_stime: the time (in number of clock ticks) that has
    //                       been spent executing the calling process
    //                       IN SUPERVISOR MODE (mostly system calls)
    //                       since its start.    
    if ((tc = times(&tmend)) == (clock_t)-1) {
        perror("Failed to get end time");
        return 1;
    }
    tcend = (unsigned long long)tc;
    
    
    // number of clock ticks spent executing function_to_time in user mode
    cticks_u = tmend.tms_utime - tmstart.tms_utime;
    
    // number of clock ticks spent executing function_to_time in supervisor mode
    cticks_s = tmend.tms_stime - tmstart.tms_stime;
    
    // total number of clock ticks spent executing function_to_time
    cticks = cticks_u + cticks_s;
    
    printf("User CPU time for operation is %f seconds\n", cticks_u/clockticks);
    printf("Supervisor CPU time for operation is %f seconds\n",
           cticks_s/clockticks);
    printf("Total CPU time for operation is %f seconds\n", cticks/clockticks);
    
    // this check is aimed at catching some errors caused by overflows
    if (tcend <= tcstart) {
        fprintf(stderr, "Couldn't calculate fraction (tick time wrapped, or too small interval)\n");
        return 1;
    }
    
    // number of clock ticks that have elapsed between the start and the end
    // of function_to_time
    wall_clock_time = tcend - tcstart;
    
    // finally, we compute the fraction of the wall clock CPU time used for
    // the execution of function_to_time
    printf("Fraction of CPU time used is %f\n", cticks/wall_clock_time);
    return 0;
}
