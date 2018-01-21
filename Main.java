package com.company;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.Scanner;

public class Main {

    private static final int DEFAULT_INSTRUMENT = 1;

    private MidiChannel channel;

    public Main() throws MidiUnavailableException {
        this(DEFAULT_INSTRUMENT);
    }

    public Main(int instrument) throws MidiUnavailableException {
        channel = getChannel(instrument);
    }

    public void setInstrument(int instrument) {
        channel.programChange(instrument);
    }

    public int getInstrument() {
        return channel.getProgram();
    }

    public void play(final int note, final int velocity) {
        channel.noteOn(note, velocity);
    }

    public void release(final int note, final int velocity) {
        channel.noteOff(note, velocity);
    }

    public void play(final int note, final long length, final int velocity) throws InterruptedException {
        play(note, velocity);
        Thread.sleep(length);
        release(note, velocity);
    }

    public void stop() {
        channel.allNotesOff();
    }

    private static MidiChannel getChannel(int instrument) throws MidiUnavailableException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        return synthesizer.getChannels()[instrument];
    }

    public static void main(String[] args) throws Exception {
        Main player = new Main();
        Scanner input = new Scanner(System.in);
        int i=0;
        char status_byte_instrument, data_byte_1_instrument, status_byte, data_byte_1, data_byte_2;
        while (!Thread.currentThread().isInterrupted()) {

            status_byte_instrument = input.next().charAt(0);//channel
            data_byte_1_instrument = input.next().charAt(0);//instrument

            //mockito ergo sum//
            data_byte_1_instrument = 0x21;
            //mockito ergo sum//

            player.setInstrument((int)data_byte_1_instrument);

            status_byte = input.next().charAt(0);//key on | key off
            data_byte_1 = input.next().charAt(0);//note
            data_byte_2 = input.next().charAt(0);//velocity

            //mockito ergo sum//
            if(i%2 == 0){
                status_byte = 0x92;
            }else{
                status_byte = 0x82;
            }
            char column = 5;
            char row = 2;
            data_byte_1 = (char)(52 + column + 5*row);
            data_byte_2 = 0x40;
            i++;
            //mockito ergo sum//

            if(status_byte == 0x92){
                player.play(data_byte_1, data_byte_2);
            }else{
                player.release(data_byte_1, data_byte_2);
            }

            //Thread.sleep(500);
        }
    }
}

