
import javax.sound.midi.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author taifun
 */
public class Synth {

    private Synthesizer synth;
    private Soundbank soundbank;
    MidiChannel channel;
    static final int CHANNEL_DRUMKITS = 9;                                               //default channel for drumkits in JAVA midi API
    static final int CHANNEL_OTHER = 0;
    static final int PROGRAM_DRUMKIT_STANDARD = 0;
    static final int PROGRAM_DRUMKIT_ROOM = 8;
    static final int PROGRAM_DRUMKIT_POWER = 16;
    static final int PROGRAM_DRUMKIT_ELECTRONIC = 24;
    static final int PROGRAM_DRUMKIT_TR808 = 25;
    static final int PROGRAM_DRUMKIT_JAZZ = 32;
    static final int PROGRAM_DRUMKIT_BRUSH = 40;
    static final int PROGRAM_DRUMKIT_ORCHESTRA = 48;
    static final int PROGRAM_DRUMKIT_SFX = 56;
    static final int KEY_BASS_DRUM = 35;
    static final int KEY_BASS_DRUM_DEEP = 36;

    Synth(int channelNumber) {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            soundbank = synth.getDefaultSoundbank();
            synth.loadAllInstruments(soundbank);
            this.setChannel(channelNumber);                                   //only one channel per synth
        } catch (Exception e) {
            System.out.println("Some shit happened with the synth.");
        }
    }

    Synth() {
        this(CHANNEL_DRUMKITS);
    }

    public void showInstruments() {
        Instrument[] instrumentList = soundbank.getInstruments();
        for (Instrument instrument : instrumentList) {
            System.out.println(instrument);
        }
    }

    public void setChannel(int channelNumber) {
        channel = synth.getChannels()[channelNumber];
    }

    public void setProgram(int program) {
        channel.programChange(program);
    }

    public void setProgram(int bank, int program) {

        channel.programChange(bank, program);
    }

    public int getProgram() {
        return channel.getProgram();
    }

    public void noteOn(int noteNumber, int velocity) {
            channel.noteOn(noteNumber, velocity);
    }

    public void noteOff(int noteNumber) {
        channel.noteOff(noteNumber);
    }
}
