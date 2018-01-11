from __future__ import absolute_import, division, unicode_literals, print_function

import sys
import termios
import tty

if sys.version_info.major < 3:
    import thread as _thread
else:
    import _thread

mainKey = []
row = [['1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '\''],
       ['q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']'],
       ['a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\''],
       ['z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/']]

try:
    from msvcrt import getch  # try to import Windows version
except ImportError:
    def getch():  # define non-Windows version
        fd = sys.stdin.fileno()
        old_settings = termios.tcgetattr(fd)
        try:
            tty.setraw(sys.stdin.fileno())
            ch = sys.stdin.read(1)
        finally:
            termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
        return ch


def keypress():
    global char
    char = getch()


def tuning_fun(char, num):
    print("tuning")


def send_fun(char):
    print("sending")


def main():
    global char
    char = None
    _thread.start_new_thread(keypress, ())
    tuning = 0

    while True:
        if char is not None:
            try:
                if tuning < 4:
                    tuning_fun(char, tuning)
                    tuning += 1
                else:
                    print("Key pressed is " + char + "\n")
            except UnicodeDecodeError:
                print("character can not be decoded, sorry!\n")
                char = None
            _thread.start_new_thread(keypress, ())
            if char == 'q' or char == '\x1b':  # x1b is ESC
                exit()
            char = None
        # print("Program is running")
        # time.sleep(1)


if __name__ == "__main__":
    main()
