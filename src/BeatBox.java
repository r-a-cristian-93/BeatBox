
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.GroupLayout.Group;

public class BeatBox implements Serializable {

    private static final int MIN_TRACKS = 6;
    private static final int MAX_TRACKS = 10;
    private static final int MIN_STEPS = 4;
    private static final int MAX_STEPS = 32;
    private int tempo = 200;
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    GroupLayout panelLayout = new GroupLayout(panel);

    //control panel components
    JLabel stepsLabel = new JLabel("Steps  ");
    JTextField stepsField = new JTextField(String.valueOf(DrumTrack.getNumberOfSteps()), 2);
    JPanel stepsPanel = new JPanel();
    JLabel tempoLabel = new JLabel("Tempo");
    JTextField tempoField = new JTextField(String.valueOf(tempo), 2);
    JPanel tempoPanel = new JPanel();
    JButton startButton = new JButton("Start");
    JButton stopButton = new JButton("Stop");
    JButton addButton = new JButton("+");
    JButton delButton = new JButton("-");
    JPanel modPanel = new JPanel();
    JButton saveButton = new JButton("Save");
    JButton loadButton = new JButton("Load");

    //track groups
    Group hSeqParGroup;
    Group vParSeqGroup;

    private final Color background_default = panel.getBackground();
    private final Color background_highlight = new Color(180, 200, 220);

    public static void main(String[] args) {
        BeatBox bb = new BeatBox();
    }

    BeatBox() {
        setup();
        run();
    }

    private void run() {
        while (true) {
            for (int i = 0; i < DrumTrack.getNumberOfSteps(); i++) {
                playStep(i);
                System.out.println("play step" + i);
                try {
                    Thread.sleep(60000 / tempo);
                } catch (Exception ex) {
                }
            }
        }
    }

    private void setup() {
        stepsPanel.add(stepsLabel);
        stepsPanel.add(stepsField);
        tempoPanel.add(tempoLabel);
        tempoPanel.add(tempoField);
        modPanel.add(addButton);
        modPanel.add(delButton);

        panel.setLayout(panelLayout);
        panelLayout.setAutoCreateGaps(true);
        panelLayout.setAutoCreateContainerGaps(true);
        panelLayout.linkSize(stepsPanel, tempoPanel, startButton, stopButton, modPanel);

        hSeqParGroup = panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        vParSeqGroup = panelLayout.createSequentialGroup();

        panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup()
                .addGroup(hSeqParGroup)
                .addGroup(panelLayout.createParallelGroup()
                        .addComponent(stepsPanel)
                        .addComponent(tempoPanel)
                        .addComponent(startButton)
                        .addComponent(stopButton)
                        .addComponent(modPanel)
                        .addComponent(saveButton)
                        .addComponent(loadButton)
                )
        );

        panelLayout.setVerticalGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(vParSeqGroup)
                .addGroup(panelLayout.createSequentialGroup()
                        .addGap(10)
                        .addComponent(stepsPanel)
                        .addComponent(tempoPanel)
                        .addComponent(startButton)
                        .addComponent(stopButton)
                        .addComponent(modPanel)
                        .addComponent(saveButton)
                        .addComponent(loadButton)
                )
        );

        for (int i = 0; i < MIN_TRACKS; i++) {
            addTrack();
        }

        frame.getContentPane().add(panel);
        frame.setTitle("BeatBox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();                                                           //resize frame
        frame.setVisible(true);

        addButton.addActionListener(new AddButtonListener());
        delButton.addActionListener(new DelButtonListener());
        stepsField.addActionListener(new StepsFieldListener());
        tempoField.addActionListener(new TempoFieldListener());
        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        saveButton.addActionListener(new SaveButtonListener());
        loadButton.addActionListener(new LoadButtonListener());


        /*
        Synth testSynth = new Synth();
        testSynth.showInstruments();
         */
    }

    private void saveState() {
        try {
            FileOutputStream fs = new FileOutputStream("beatbox.ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(panel);
            os.close();
        } catch (Exception e) {
            System.out.println("Not Saved.");
            e.printStackTrace();
        }
    }

    private void loadState() {
        try {
            FileInputStream fs = new FileInputStream("beatbox.ser");
            ObjectInputStream is = new ObjectInputStream(fs);
            Object objectPanel = is.readObject();
            panel = (JPanel) objectPanel;
            is.close();
        } catch (Exception e) {
            System.out.println("Not Loaded.");
        }
    }

    private void setTempo(int tempo) {
        this.tempo = tempo;
    }

    private void linkLastTrack() {
        ArrayList<DrumTrack> trackList = getTracks();
        int lastTrack = trackList.size() - 1;
        if (lastTrack > 0) {
            panelLayout.linkSize(trackList.get(lastTrack), trackList.get(lastTrack - 1));
        }
    }

    public void addTrack() {
        ArrayList<DrumTrack> trackList = getTracks();
        int lastTrack = trackList.size();
        if (lastTrack < MAX_TRACKS) {
            DrumTrack newTrack = new DrumTrack();
            hSeqParGroup.addComponent(newTrack);
            vParSeqGroup.addComponent(newTrack);
            linkLastTrack();
            frame.pack();
        }
    }

    public void delTrack() {
        ArrayList<DrumTrack> trackList = getTracks();
        int lastTrack = trackList.size() - 1;
        if (lastTrack >= MIN_TRACKS) {
            panel.remove(trackList.get(lastTrack));
            frame.pack();                                                       //resize frame
        }
    }

    public void setSteps(int numberOfSteps) {
        if (numberOfSteps >= MIN_STEPS && numberOfSteps <= MAX_STEPS) {
            ArrayList<DrumTrack> trackList = getTracks();
            for (DrumTrack track : trackList) {
                track.setSteps(numberOfSteps);
            }
            frame.pack();
        }
    }

    public void playStep(int barNumber) {
        ArrayList<DrumTrack> trackList = getTracks();

        for (DrumTrack track : trackList) {
            ArrayList<JCheckBox> barList = new ArrayList<>();
            for (Component component : track.getComponents()) {
                if (component instanceof JCheckBox) {
                    barList.add((JCheckBox) component);
                }
            }
            if (barList.get(barNumber).isSelected()) {
                track.playKey();
            }
            //highlight bars
            if (barNumber > 0) {
                barList.get(barNumber - 1).setBackground(background_default);
            }
            if (barNumber < barList.size()) {
                barList.get(barNumber).setBackground(background_highlight);
            }
            if (barNumber == 0) {
                barList.get(barList.size() - 1).setBackground(background_default);
            }

        }
    }

    public ArrayList<DrumTrack> getTracks() {
        ArrayList<DrumTrack> trackList = new ArrayList<>();
        Component[] panelComponents = panel.getComponents();
        for (Component panelComponent : panelComponents) {
            if (panelComponent instanceof DrumTrack) {
                trackList.add((DrumTrack) panelComponent);
            }
        }
        return trackList;

    }

    class AddButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            addTrack();
        }
    }

    class DelButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            delTrack();
        }
    }

    class StepsFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String textField = stepsField.getText();
            int numberOfSteps = Helper.toNumber(0, textField);
            setSteps(numberOfSteps);
        }
    }

    class TempoFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String textField = tempoField.getText();
            int tempoValue = Helper.toNumber(tempo, textField);
            setTempo(tempoValue);
        }
    }

    class StopButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

        }
    }

    class StartButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

        }
    }

    class SaveButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            saveState();
        }
    }

    class LoadButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            loadState();
        }
    }
}
