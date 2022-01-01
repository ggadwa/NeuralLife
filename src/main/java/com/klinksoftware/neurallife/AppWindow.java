package com.klinksoftware.neurallife;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final int TOOL_BUTTON_START = 0;
    private static final int TOOL_BUTTON_PAUSE = 1;
    private static final int TOOL_BUTTON_STOP = 2;
    private static final int TOOL_BUTTON_SETUP = 3;

    private JFrame frame;
    private JToolBar toolBar;
    private JButton buttonStart, buttonStop;
    private ImageIcon iconStart, iconPause;

    private Configuration config;
    private Random random;
    private LifeCanvas lifeCanvas;
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

        switch (lifeCanvas.getSimulationState()) {
            case Simulation.STATE_RUNNING:
                lifeCanvas.simulationPause();
                buttonStart.setIcon(iconStart);
                buttonStart.setToolTipText("Start Simulation");
                break;
            case Simulation.STATE_PAUSED:
                lifeCanvas.simulationResume();
                buttonStart.setIcon(iconPause);
                buttonStart.setToolTipText("Pause Simulation");
                break;
            case Simulation.STATE_STOPPED:
                lifeCanvas.simulationStart();
                buttonStart.setIcon(iconPause);
                buttonStart.setToolTipText("Pause Simulation");
                break;
        }

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(true);
    }

    public void simulationStop() {
        buttonStop.setEnabled(false);
        lifeCanvas.simulationStop();

        buttonStart.setIcon(iconStart);
        buttonStart.setEnabled(true);
        buttonStart.setToolTipText("Start Simulation");
    }

    // toolbars
    private void toolBarClick(int buttonId) {
        switch (buttonId) {
            case TOOL_BUTTON_START:
                simulationStart();
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

    private JButton addToolButton(int buttonId, boolean enabled, ImageIcon imageIcon, String toolTipText) {
        JButton button;

        button = new JButton();
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setIcon(imageIcon);
        button.setEnabled(enabled);
        button.setToolTipText(toolTipText);
        button.addActionListener(e -> toolBarClick(buttonId));

        toolBar.add(button);

        return (button);
    }

    // start main window
    public void start() {
        int wid;
        int high;
        URL iconURL;
        Image image;
        Runnable loop;

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

        // life canvas size
        wid = (config.setup.gridXCount + 2) * config.setup.gridSize;
        high = (config.setup.gridYCount + 2) * config.setup.gridSize;

        // create the window
        frame = new JFrame();

        frame.setTitle("NeuralLife");
        frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());

        // some toolbar icons
        iconStart = new ImageIcon(getClass().getResource("/icons/tool_start.png"));
        iconPause = new ImageIcon(getClass().getResource("/icons/tool_pause.png"));

        // toolbar
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, TOOLBAR_HEIGHT));
        buttonStart = addToolButton(TOOL_BUTTON_START, true, iconStart, "Start Simulation");
        buttonStop = addToolButton(TOOL_BUTTON_STOP, false, new ImageIcon(getClass().getResource("/icons/tool_stop.png")), "Stop Simulation");
        toolBar.addSeparator();
        addToolButton(TOOL_BUTTON_SETUP, true, new ImageIcon(getClass().getResource("/icons/tool_setup.png")), "Simulation Setup");
        frame.add(toolBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        // life canvas
        lifeCanvas = new LifeCanvas(config, random);
        lifeCanvas.setSize(wid, high);
        lifeCanvas.setPreferredSize(new Dimension(wid, high));
        lifeCanvas.setFocusable(false);

        frame.add(lifeCanvas, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        // all the event listeners
        frame.addWindowListener(this);

        // show the window
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // canvas initialize
        lifeCanvas.initialize();

        // setup dialog
        setupDialog = new SetupDialog(frame, config);
        setupDialog.initialize();
    }

    public void resize() {
        int wid, high;

        // stop the simulation
        simulationStop();

        // resize window
        wid = (config.setup.gridXCount + 2) * config.setup.gridSize;
        high = (config.setup.gridYCount + 2) * config.setup.gridSize;

        lifeCanvas.setSize(wid, high);
        lifeCanvas.setPreferredSize(new Dimension(wid, high));

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    // stop main window
    public void stop() {
        simulationStop();
        frame.dispose();
    }
}
