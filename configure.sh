#!/bin/bash

stty -F $1 115200 raw -clocal -echo icrnl
chmod +r $1
chmod +r $2

