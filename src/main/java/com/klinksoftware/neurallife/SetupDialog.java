package com.klinksoftware.neurallife;

import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.text.NumberFormatter;

public class SetupDialog extends JDialog {

    private static final int DIALOG_WIDTH = 500;
    private static final int DIALOG_HEIGHT = 400;

    private Configuration config;
    private JSlider setupMillisecondPerStep, setupWorldWidth, setupWorldHeight;
    private JSlider robotInitialFuelCount, robotReproduceFuelCount,
            robotSightSweep, robotSightDistance;
    private JSlider robotDriveSurvival, robotDriveReproduce, robotDriveEat, robotDriveCuriosity;
    private JSlider monsterCount, monsterMinStartDistanceFromRobot,
            monsterSightSweep, monsterSightDistance,
            monsterMaxChaseStepCount, monsterChaseCoolDownStepCount;
    private JSlider foodPerStepAddRate, foodInitialCount, foodMaxCount, foodFuelPerFood;
    private JSlider miscRockCount;

    public SetupDialog(Frame parent, Configuration config) {
        super(parent, true);
        this.config = config;
    }

    private void controlsToConfig() {
        config.setup.millisecondPerStep = (int) setupMillisecondPerStep.getValue();
        config.setup.worldWidth = (int) setupWorldWidth.getValue();
        config.setup.worldHeight = (int) setupWorldHeight.getValue();

        config.robot.initialFuelCount = (int) robotInitialFuelCount.getValue();
        config.robot.reproduceFuelCount = (int) robotReproduceFuelCount.getValue();
        config.robot.sightSweep = (int) robotSightSweep.getValue();
        config.robot.sightDistance = (int) robotSightDistance.getValue();

        config.robot.driveSurvival = (int) robotDriveSurvival.getValue();
        config.robot.driveReproduce = (int) robotDriveReproduce.getValue();
        config.robot.driveEat = (int) robotDriveEat.getValue();
        config.robot.driveCuriosity = (int) robotDriveCuriosity.getValue();

        config.monster.count = (int) monsterCount.getValue();
        config.monster.minStartDistanceFromRobot = (int) monsterMinStartDistanceFromRobot.getValue();
        config.monster.sightSweep = (int) monsterSightSweep.getValue();
        config.monster.sightDistance = (int) monsterSightDistance.getValue();
        config.monster.maxChaseStepCount = (int) monsterMaxChaseStepCount.getValue();
        config.monster.chaseCoolDownStepCount = (int) monsterChaseCoolDownStepCount.getValue();

        config.food.perStepAddRate = (int) foodPerStepAddRate.getValue();
        config.food.initialCount = (int) foodInitialCount.getValue();
        config.food.maxCount = (int) foodMaxCount.getValue();
        config.food.fuelPerFood = (int) foodFuelPerFood.getValue();

        config.misc.rockCount = (int) miscRockCount.getValue();
    }

    private JFormattedTextField addControl(JPanel controls, int y, int min, int max, String label) {
        JFormattedTextField field;
        NumberFormatter formatter;

        formatter = new NumberFormatter(NumberFormat.getInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(min);
        formatter.setMaximum(max);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        controls.add(new JLabel(label), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        field = new JFormattedTextField(formatter);
        field.setColumns(10);
        controls.add(field, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));

        return (field);
    }

    private JSlider addSlider(JPanel tab, int row, int min, int max, boolean showTicks, String label) {
        int majorTick;
        JSlider field;
        NumberFormatter formatter;

        tab.add(new JLabel(label), new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));

        majorTick = (max - min) / 5;

        field = new JSlider(JSlider.HORIZONTAL, min, max, min);
        field.setMajorTickSpacing(majorTick);
        field.setMinorTickSpacing(majorTick / 10);
        field.setPaintTicks(showTicks);
        field.setPaintLabels(showTicks);
        tab.add(field, new GridBagConstraints(1, row, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));

        return (field);
    }

    public void initialize() {
        JTabbedPane tab;
        JPanel dialog, setupTab, robotSetupTab, robotDriveTab, monsterTab, foodTab, miscTab, buttons;
        JButton okButton, cancelButton;

        setTitle("Simulation Setup");
        setResizable(false);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

        // the panes
        dialog = new JPanel(new BorderLayout(3, 3));
        dialog.setBounds(5, 5, 5, 5);

        // the tabs
        tab = new JTabbedPane();
        dialog.add(tab, BorderLayout.CENTER);

        // setup tab
        setupTab = new JPanel(new GridBagLayout());
        tab.add("Setup", setupTab);

        setupMillisecondPerStep = addSlider(setupTab, 0, 1000, 6000, true, "Millisecond Per Step:");
        setupWorldWidth = addSlider(setupTab, 1, 500, 1000, true, "World Width:");
        setupWorldHeight = addSlider(setupTab, 2, 500, 1000, true, "World Height:");

        // robot tab
        robotSetupTab = new JPanel(new GridBagLayout());
        tab.add("Robot Setup", robotSetupTab);

        robotInitialFuelCount = addSlider(robotSetupTab, 0, 0, 1000, true, "Initial Fuel Count:");
        robotReproduceFuelCount = addSlider(robotSetupTab, 1, 0, 1000, true, "Reproduce Fuel Count:");
        robotSightSweep = addSlider(robotSetupTab, 2, 0, 225, true, "Sight Sweep Angle:");
        robotSightDistance = addSlider(robotSetupTab, 3, 0, 500, true, "Sight Distance:");

        // drive tab
        robotDriveTab = new JPanel(new GridBagLayout());
        tab.add("Robot Drive", robotDriveTab);

        robotDriveSurvival = addSlider(robotDriveTab, 0, 0, 100, false, "Survival:");
        robotDriveReproduce = addSlider(robotDriveTab, 1, 0, 100, false, "Reproduce:");
        robotDriveEat = addSlider(robotDriveTab, 2, 0, 100, false, "Eat:");
        robotDriveCuriosity = addSlider(robotDriveTab, 3, 0, 100, false, "Curiosity:");

        // monster tab
        monsterTab = new JPanel(new GridBagLayout());
        tab.add("Monster", monsterTab);

        monsterCount = addSlider(monsterTab, 0, 0, 100, true, "Count:");
        monsterMinStartDistanceFromRobot = addSlider(monsterTab, 1, 0, 1000, true, "Min Start Distance from Robot:");
        monsterSightSweep = addSlider(monsterTab, 2, 0, 225, true, "Sight Sweep Angle:");
        monsterSightDistance = addSlider(monsterTab, 3, 0, 500, true, "Sight Distance:");
        monsterMaxChaseStepCount = addSlider(monsterTab, 4, 0, 100, true, "Max Chase Step Count:");
        monsterChaseCoolDownStepCount = addSlider(monsterTab, 5, 0, 100, true, "Chase Cool Down Step Count:");

        // food tab
        foodTab = new JPanel(new GridBagLayout());
        tab.add("Food", foodTab);

        foodPerStepAddRate = addSlider(foodTab, 0, 0, 100, true, "Add Rate (by step):");
        foodInitialCount = addSlider(foodTab, 1, 0, 100, true, "Initial Count:");
        foodMaxCount = addSlider(foodTab, 2, 0, 100, true, "Max Count:");
        foodFuelPerFood = addSlider(foodTab, 3, 0, 1000, true, "Fuel Per Food:");

        // misc tab
        miscTab = new JPanel(new GridBagLayout());
        tab.add("Misc", miscTab);

        miscRockCount = addSlider(miscTab, 0, 0, 100, true, "Rock Count:");

        // the buttons
        buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        dialog.add(buttons, BorderLayout.SOUTH);

        okButton = new JButton("OK");
        okButton.addActionListener(
                event -> {
                    controlsToConfig();
                    NeuralLife.appWindow.configurationSave();
                    setVisible(false);
                    NeuralLife.appWindow.resize();
                });
        buttons.add(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(
                event -> {
                    setVisible(false);
                });
        buttons.add(cancelButton);

        // finish dialog
        setContentPane(dialog);
    }

    public void open() {
        // setup
        setupMillisecondPerStep.setValue(config.setup.millisecondPerStep);
        setupWorldWidth.setValue(config.setup.worldWidth);
        setupWorldHeight.setValue(config.setup.worldHeight);

        // robot setup
        robotInitialFuelCount.setValue(config.robot.initialFuelCount);
        robotReproduceFuelCount.setValue(config.robot.reproduceFuelCount);
        robotSightSweep.setValue(config.robot.sightSweep);
        robotSightDistance.setValue(config.robot.sightDistance);

        // robot drive
        robotDriveSurvival.setValue(config.robot.driveSurvival);
        robotDriveReproduce.setValue(config.robot.driveReproduce);
        robotDriveEat.setValue(config.robot.driveEat);
        robotDriveCuriosity.setValue(config.robot.driveCuriosity);

        // monster
        monsterCount.setValue(config.monster.count);
        monsterMinStartDistanceFromRobot.setValue(config.monster.minStartDistanceFromRobot);
        monsterSightSweep.setValue(config.monster.sightSweep);
        monsterSightDistance.setValue(config.monster.sightDistance);
        monsterMaxChaseStepCount.setValue(config.monster.maxChaseStepCount);
        monsterChaseCoolDownStepCount.setValue(config.monster.chaseCoolDownStepCount);

        // food
        foodPerStepAddRate.setValue(config.food.perStepAddRate);
        foodInitialCount.setValue(config.food.initialCount);
        foodMaxCount.setValue(config.food.maxCount);
        foodFuelPerFood.setValue(config.food.fuelPerFood);

        // misc
        miscRockCount.setValue(config.misc.rockCount);

        // show window
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

}
