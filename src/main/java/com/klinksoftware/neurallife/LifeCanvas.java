package com.klinksoftware.neurallife;

import com.klinksoftware.neurallife.simulation.Configuration;
import com.klinksoftware.neurallife.simulation.Simulation;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class LifeCanvas extends Canvas {

    private final Configuration config;
    private final Random random;
    private final InfoPanel infoPanel;
    private BufferStrategy buffStrat;
    private Simulation simulation;

    public LifeCanvas(Configuration config, Random random, InfoPanel infoPanel) {
        super();

        this.config = config;
        this.random = random;
        this.infoPanel = infoPanel;

        setBackground(Color.BLACK);

        // the simulation
        simulation = new Simulation(config, random, this, infoPanel);
    }

    public void initialize() {
        // buffer strategy
        createBufferStrategy(2);
        buffStrat = getBufferStrategy();
    }

    // simulation
    public void simulationStart() {
        simulation.start();
    }

    public void simulationPause() {
        simulation.pause();
    }

    public void simulationResume() {
        simulation.resume();
    }

    public void simulationStop() {
        simulation.stop();
    }

    public int getSimulationState() {
        return (simulation.getState());
    }

    public Simulation getSimulation() {
        return (simulation);
    }

    public void draw() {
        Graphics g;

        // do the buffer strategy dance
        do {

            // draw to the buffer
            do {
                g = buffStrat.getDrawGraphics();
                simulation.draw((Graphics2D) g);
                g.dispose();
            } while (buffStrat.contentsRestored());

            // flip the buffer
            buffStrat.show();

        } while (buffStrat.contentsLost());
    }

}
