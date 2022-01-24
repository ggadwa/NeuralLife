package com.klinksoftware.neurallife.simulation;

import com.klinksoftware.neurallife.InfoPanel;
import com.klinksoftware.neurallife.LifeCanvas;
import java.awt.Graphics2D;
import java.util.Random;

public class Simulation implements Runnable {

    public static final int STATE_RUNNING = 0;
    public static final int STATE_PAUSED = 1;
    public static final int STATE_STOPPING = 2;
    public static final int STATE_STOPPED = 3;

    private int state;
    private int step;
    private long nextTick;
    private Thread thread;
    private final Configuration config;
    private final Random random;
    private final LifeCanvas lifeCanvas;
    private final InfoPanel infoPanel;
    private Board board;

    public Simulation(Configuration config, Random random, LifeCanvas lifeCanvas, InfoPanel infoPanel) {
        this.config = config;
        this.random = random;
        this.lifeCanvas = lifeCanvas;
        this.infoPanel = infoPanel;

        board = new Board(config, random, lifeCanvas);

        state = STATE_STOPPED;
    }

    // simulation controls
    public void start() {
        step = 0;
        nextTick = System.currentTimeMillis();
        state = STATE_RUNNING;

        board = new Board(config, random, lifeCanvas);
        board.startup();

        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        state = STATE_PAUSED;
    }

    public void resume() {
        nextTick = System.currentTimeMillis();
        state = STATE_RUNNING;
    }

    public void stop() {
        state = STATE_STOPPING;

        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {  // nothing to do here
        }
        thread = null;

        state = STATE_STOPPED;
    }

    public int getState() {
        return (state);
    }

    // getters
    public Board getBoard() {
        return (board);
    }

    public int getStep() {
        return (step);
    }

    // draw
    public void draw(Graphics2D g) {
        board.draw(g, step);
    }

    // run loop
    @Override
    public void run() {
        long tick;

        while (true) {
            // special stopping state, kill this thread
            if (state == STATE_STOPPING) {
                return;
            }

            // do nothing if not running
            if (state != STATE_RUNNING) {
                continue;
            }

            // time for another step?
            tick = System.currentTimeMillis();
            if (tick < nextTick) {
                continue;
            }

            nextTick = tick + (long) config.setup.millisecondPerStep;

            // run the simulation step
            board.runStep(step);

            // repaint
            lifeCanvas.draw();

            // update  info
            infoPanel.updateStats(this);

            // move to next step
            step++;
        }
    }

}
