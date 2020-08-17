
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DrumTrack extends JPanel {

    private static int number_of_bars = 8;
    private int keyNumber = 36;
    private int velocityValue = 100;
    Synth synth;                                //should be private
    private JComboBox programDropdown;
    private JTextField keyField;
    private JTextField velocityField;
    private FlowLayout layout;
    private String[] programDropdownList;

    DrumTrack() {
        synth = new Synth();
        setupPanel();
    }

    private void setupPanel() {
        programDropdownList = new String[]{"0", "8", "16", "24", "25", "32", "40", "48", "56"};
        programDropdown = new JComboBox(programDropdownList);
       
        keyField = new JTextField(String.valueOf(keyNumber), 2);
        velocityField = new JTextField(String.valueOf(velocityValue), 2);
        layout = new FlowLayout();
        layout.setAlignment(FlowLayout.TRAILING);
        
        this.setLayout(layout);
        this.add(programDropdown);
        this.add(keyField);
        this.add(velocityField);
        this.setSteps(number_of_bars);

        programDropdown.addActionListener(new ProgramChangeListener());
        keyField.addActionListener(new KeyFieldListener());
        velocityField.addActionListener(new VelocityFieldListener());
    }

    public void setSteps(int steps) {
        Component[] componentList = this.getComponents();
        ArrayList<JCheckBox> barList = new ArrayList<JCheckBox>();

        for (Component component : componentList) {
            if (component instanceof JCheckBox) {
                barList.add((JCheckBox) component);
            }
        }

        if (steps > barList.size()) {
            for (int i = barList.size(); i < steps; i++) {
                this.add(new JCheckBox());
            }
            number_of_bars = steps;
        }

        if (steps < barList.size()) {
            for (int i = barList.size(); i > steps; i--) {
                this.remove(i + 1);
            }
            number_of_bars = steps;
        }
    }

    public void playKey() {
        synth.noteOn(keyNumber, velocityValue);
    }

    public static int getNumberOfSteps() {
        return number_of_bars;
    }

    class ProgramChangeListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int programNumber = Integer.parseInt((String) programDropdown.getSelectedItem());
            synth.setProgram(programNumber);
            System.out.println("program set to " + programNumber);
        }
    }

    class KeyFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String fieldText = keyField.getText();
            keyNumber = Helper.toNumber(keyNumber, fieldText);
            System.out.println("key set to: " + keyNumber);
        }
    }

    class VelocityFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String fieldText = velocityField.getText();
            velocityValue = Helper.toNumber(velocityValue, fieldText);
            System.out.println("velocity set to: " + velocityValue);
        }
    }
}
