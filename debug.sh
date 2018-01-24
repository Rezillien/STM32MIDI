#!/bin/bash
sudo python ./KeyboardGuitar/scriptv2.py `ls /dev | grep USB | tail -1` &
