package com.klinksoftware.neurallife;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import com.klinksoftware.neurallife.simulation.Simulation;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Calendar;
import java.util.Random;
import javax.swing.*;

public class AppWindow implements WindowListener {

    public static final String DATA_BASE_PATH = "data" + File.separator;
    public static final String SETUP_NAME = "setup.json";

    public static final int TOOLBAR_HEIGHT = 38;
    public static final int INFO_WIDTH = 250;

    private static final int TOOL_BUTTON_START = 0;
    private static final int TOOL_BUTTON_PAUSE = 1;
    private static final int TOOL_BUTTON_STOP = 2;
    private static final int TOOL_BUTTON_SETUP = 3;

    private JFrame frame;
    private JToolBar toolBar;
    private JButton buttonStart, buttonPause, buttonStop;

    private Configuration config;
    private Random random;
    private LifeCanvas lifeCanvas;
    private InfoPanel infoPanel;
    private SetupDialog setupDialog;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // directory creation
    public void createRequiredDirectories() {
        (new File(DATA_BASE_PATH)).mkdirs();
    }

    // load and save preferences
    public void configurationLoad() {
        File file;

        file = new File(DATA_BASE_PATH + SETUP_NAME);
        try {
            config = objectMapper.readValue(file, Configuration.class);
        } catch (IOException e) {
            config = new Configuration();
        }
    }

    public void configurationSave() {
        File file;

        file = new File(DATA_BASE_PATH + SETUP_NAME);
        try {
            objectMapper.writeValue(file, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // window events
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        NeuralLife.stop();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    // simulation controls
    public void simulationStart() {
        buttonStart.setEnabled(false);
        buttonPause.setEnabled(true);
        buttonStop.setEnabled(true);
        lifeCanvas.simulationStart();
    }

    public void simulationPause() {
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonStop.setEnabled(true);
        lifeCanvas.simulationPause();
    }

    public void simulationResume() {
        buttonStart.setEnabled(false);
        buttonPause.setEnabled(true);
        buttonStop.setEnabled(true);
        lifeCanvas.simulationResume();
    }

    public void simulationStop() {
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonStop.setEnabled(false);
        lifeCanvas.simulationStop();
    }

    // toolbars
    private void toolBarClick(int buttonId) {
        switch (buttonId) {
            case TOOL_BUTTON_START:
                if (lifeCanvas.getSimulationState() == Simulation.STATE_PAUSED) {
                    simulationResume();
                } else {
                    simulationStart();
                }
                break;
            case TOOL_BUTTON_PAUSE:
                simulationPause();
                break;
            case TOOL_BUTTON_STOP:
                simulationStop();
                break;
            case TOOL_BUTTON_SETUP:
                simulationStop();
                setupDialog.open();
                break;
        }
    }

    private JButton addToolButton(int buttonId, boolean enabled, String imageName, String toolTipText) {
        JButton button;

        button = new JButton();
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setIcon(new ImageIcon(getClass().getResource("/icons/" + imageName + ".png")));
        button.setEnabled(enabled);
        button.setToolTipText(toolTipText);
        button.addActionListener(e -> toolBarClick(buttonId));

        toolBar.add(button);

        return (button);
    }

    // start main window
    public void start() {
        URL iconURL;
        Image image;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        // required directories
        createRequiredDirectories();

        // setup random
        random = new Random(Calendar.getInstance().getTimeInMillis());

        // load the configuration
        config = new Configuration();
        configurationLoad();

        // window icon
        iconURL = getClass().getResource("/icons/app_icon.png");
        image = new ImageIcon(iconURL).getImage();

        // the quit menu event and doc icon
        // this is only handled on some OSes, so we just ignore if
        // it errors out
        try {
            Desktop.getDesktop().setQuitHandler((event, response) -> NeuralLife.stop());
            Taskbar.getTaskbar().setIconImage(image);
        } catch (Exception e) {
        }

        // create the window
        frame = new JFrame();

        frame.setTitle("NeuralLife");
        frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setPreferredSize(new Dimension((config.setup.worldWidth + INFO_WIDTH), (config.setup.worldHeight + TOOLBAR_HEIGHT)));
        frame.pack();   // this is a trick to force it to set the content to the exact size we want without knowing window chrome size
        frame.setLayout(null);

        // toolbar
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBounds(0, 0, (config.setup.worldWidth + INFO_WIDTH), TOOLBAR_HEIGHT);
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        buttonStart = addToolButton(TOOL_BUTTON_START, true, "tool_start", "Start Simulation");
        buttonPause = addToolButton(TOOL_BUTTON_PAUSE, false, "tool_pause", "Pause Simulation");
        buttonStop = addToolButton(TOOL_BUTTON_STOP, false, "tool_stop", "Stop Simulation");
        toolBar.addSeparator();
        addToolButton(TOOL_BUTTON_SETUP, true, "tool_setup", "Simulation Setup");

        frame.add(toolBar);

        // info
        infoPanel = new InfoPanel();
        infoPanel.setBounds(config.setup.worldWidth, TOOLBAR_HEIGHT, INFO_WIDTH, (config.setup.worldHeight + Board.STATUS_BAR_HEIGHT));
        infoPanel.addControls();

        frame.add(infoPanel);

        // life canvas
        lifeCanvas = new LifeCanvas(config, random, infoPanel);
        lifeCanvas.setFocusable(false);
        lifeCanvas.setBounds(0, TOOLBAR_HEIGHT, config.setup.worldWidth, (config.setup.worldHeight + Board.STATUS_BAR_HEIGHT));

        frame.add(lifeCanvas);

        // all the event listeners
        frame.addWindowListener(this);

        // show the window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // canvas initialize
        lifeCanvas.initialize();

        // setup dialog
        setupDialog = new SetupDialog(frame, config);
        setupDialog.initialize();
    }

    public void resize() {
        // stop the simulation
        simulationStop();

        // resize window
        frame.getContentPane().setPreferredSize(new Dimension((config.setup.worldWidth + INFO_WIDTH), (config.setup.worldHeight + TOOLBAR_HEIGHT)));
        frame.pack();   // this is a trick to force it to set the content to the exact size we want without knowing window chrome size

        // and canvas
        lifeCanvas.setBounds(0, TOOLBAR_HEIGHT, config.setup.worldWidth, (config.setup.worldHeight + Board.STATUS_BAR_HEIGHT));

        frame.setLocationRelativeTo(null);
    }

    // stop main window
    public void stop() {
        simulationStop();
        frame.dispose();
    }
}
