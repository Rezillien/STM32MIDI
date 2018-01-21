import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.io.FileInputStream;
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
        FileInputStream in = null;

        in = new FileInputStream("/dev/ttyACM0");
        byte[] bytes = new byte[1];


        System.out.println("blop");
        Main player = new Main();
        boolean flag = true;
        int status_byte_instrument, data_byte_1_instrument, status_byte, data_byte_1, data_byte_2;
        while (!Thread.currentThread().isInterrupted()) {

            status_byte_instrument =in.read();//channel

            while(flag){
                if(status_byte_instrument == 0xF0) {
                    data_byte_1 = in.read();//note
                    data_byte_2 = in.read();//velocity
                    if (!(data_byte_1 == 0xFF && data_byte_2 == 0xFF)) {
                        continue;
                    }
                    flag = false;
                    status_byte_instrument = in.read();//channel
                }
            }

	        if(status_byte_instrument == 0xC2){

                data_byte_1_instrument = in.read();//instrument


                player.setInstrument(data_byte_1_instrument);
                System.out.println("status " + status_byte_instrument + " " + data_byte_1_instrument);

	        } else if(status_byte_instrument == 0x92 || status_byte_instrument == 0x82){
                status_byte = status_byte_instrument;//key on | key off
                data_byte_1 = in.read();//note
                data_byte_2 = in.read();//velocity


                if(status_byte == 0x92) {
                    player.play(data_byte_1, data_byte_2);
                }else {
                    player.release(data_byte_1, data_byte_2);
                }
                System.out.println("note " + status_byte + " " + data_byte_1 + " " + data_byte_2);
	        }

            //Thread.sleep(500);
        }
    }
}
