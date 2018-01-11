from tkinter import *
import serial

import os


ser = serial.Serial('/dev/serial/by-id/usb-FTDI_FT232R_USB_UART_A702HE61-if00-port0', 115200)  # open serial port

bass_num = 33

rows = [['1', '2', '3', '4', '5', '6', '7', '8', '9', '0'],
        ['q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'],
        ['a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';'],
        ['z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/']]

main_keys = []

active_keys = []

played_rows = []

start_val = 0

midi_per_row = [[0, 0, 0],
                [0, 0, 0],
                [0, 0, 0],
                [0, 0, 0]]


def calculate_chr(status, row, index):
    end_chr = 0
    if status:
        end_chr = end_chr + 64
    end_chr = end_chr + (row * 16) + index
    return chr(end_chr)


def send_up(row, index):
    ser.write(calculate_chr(True, row, index))
    print(row, ' ', index)


def keyup(e):
    for i in played_rows:
        if e.char == rows[i[1]][i[2] - 1]:
            send_up(i[1], i[2])
            played_rows.remove(i)
            break
    if e.char in active_keys:
        active_keys.remove(e.char)


def send_down(row, index):
    ser.write(calculate_chr(False, row, index))
    print(row, ' ', index)


def keydown(e):
    global start_val
    if start_val < 4:
        if e.char in main_keys:
            print('chose other key')
            return
        main_keys.append(e.char)
        start_val += 1
    else:
        if e.char in main_keys:
            row = main_keys.index(e.char)
            active = rows[row]
            max = -1
            for i in active_keys:
                if i in active and max < active.index(i):
                    max = active.index(i)
            if max != -1:
                played_rows.append([e.char, row, max + 1])
            max += 1
            send_down(row, max)

        else:
            for i in rows:
                if e.char in i:
                    active_keys.append(e.char)


def on_closing():
    os.system('xset r on')
    ser.close()
    root.destroy()


root = Tk()
os.system('xset r off')
frame = Frame(root, width=100, height=100)
root.protocol("WM_DELETE_WINDOW", on_closing)
frame.bind("<KeyPress>", keydown)
frame.bind("<KeyRelease>", keyup)
frame.pack()
frame.focus_set()
root.mainloop()
