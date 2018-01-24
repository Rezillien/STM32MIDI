#!/bin/bash
sudo ttymidi -s /dev/`ls /dev | grep ACM | tail -1` -b 115200 &
qsynth --server --audio-driver=alsa -o audio.alsa.device=hw:1 /usr/share/soundfonts/FluidR3_GM.sf2 &
sleep 2
sudo aconnect `aconnect -i | grep ttymidi | awk -F':' '{print $1}' | tr -dc '0-9'`:0 `aconnect -o | grep FLUID | awk -F':' '{print $1}' | tr -dc '0-9'`:0


