#include <linux/soundcard.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>

int main(void) {
    char* device =  "/dev/snd/hwC1D0";
    char* stm = "/dev/ttyACM0";
    unsigned char data[3] = {0x90, 60, 127};

    // step 1: open the OSS device for writing
    int fd1 = open(device, O_WRONLY, 0);
    int fd2 = open(stm, O_RDONLY, 0);

    if (fd1 < 0 || fd2 < 0) {
        printf("Error: cannot open %s\n", device);
        return 1;
    }

    while(1) {
        int num = read(fd2,data,sizeof(data));
        printf("%c%c%c\n",data[0],data[1],data[2]);
        // step 2: write the MIDI information to the OSS device
        write(fd1, data, num*sizeof(unsigned char));
    }

    // step 3: (optional) close the OSS device

    return 0;
}