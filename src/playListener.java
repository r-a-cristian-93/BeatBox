
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class playListener implements ActionListener {

    Synth synth;
    int noteNumber;

    public void actionPerformed(ActionEvent e) {
        synth.noteOn(noteNumber, 100);
    }

    playListener(Synth synth, int noteNumber) {
        this.synth = synth;
        this.noteNumber = noteNumber;
    }
}
