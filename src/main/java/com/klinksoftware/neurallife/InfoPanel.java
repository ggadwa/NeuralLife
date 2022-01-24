package com.klinksoftware.neurallife;

import com.klinksoftware.neurallife.simulation.Simulation;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {

    private static final int NAME_WIDTH = 150;
    private static final int VALUE_WIDTH = 100;
    private static final int LINE_HEIGHT = 25;

    private static final Color EVEN_LINE_COLOR = new Color(0.9f, 0.9f, 1.0f);
    private static final Color ODD_LINE_COLOR = new Color(0.7f, 0.7f, 1.0f);

    private JLabel stepValue;

    public InfoPanel() {
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
        setLayout(null);
    }

    public JLabel addSingleControlLine(int y, String name, String value) {
        int top;
        boolean even;
        JLabel nameLabel, valueLabel;

        top = y * LINE_HEIGHT;
        even = (y & 0x1) == 0;

        nameLabel = new JLabel(name + ": ");
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);
        nameLabel.setBounds(1, top, NAME_WIDTH, LINE_HEIGHT);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(even ? EVEN_LINE_COLOR : ODD_LINE_COLOR);
        add(nameLabel);

        valueLabel = new JLabel(value);
        valueLabel.setBounds((NAME_WIDTH + 1), top, (VALUE_WIDTH - 1), LINE_HEIGHT);
        valueLabel.setOpaque(true);
        valueLabel.setBackground(even ? EVEN_LINE_COLOR : ODD_LINE_COLOR);
        add(valueLabel);

        return (valueLabel);
    }

    public void addControls() {
        stepValue = addSingleControlLine(0, "Step", "0");
        addSingleControlLine(1, "Test", "1");
        addSingleControlLine(2, "Test2", "2");
    }

    public void updateStats(Simulation simulation) {
        stepValue.setText(Integer.toString(simulation.getStep()));
    }
}
