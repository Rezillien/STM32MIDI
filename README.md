# STM32MIDI

## STM32MIDI is simple interface to create your own MIDI instrument using STM32F429I

### Requirements
- [ttymidi](https://github.com/robelix/hard-dj/tree/master/ttymidi)
- [FluidSynth](https://github.com/FluidSynth/fluidsynth)
- OpenOCD
- arm-none-eabi
- Python 3


### Building
```
clone project and go to main directory
make program
./connect.sh
```

### Additional info
Project include sample instrument script that use uart to send data to stm. See KeyboardGuitar directory or run ./guitar.sh


