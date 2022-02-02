package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Color;
import java.util.Random;

public class ItemFood extends Item {

    private boolean eaten;

    public ItemFood(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board, "food", new Color(0.0f, 0.0f, 0.0f));

        pnt = board.getRandomPoint();
        eaten = false;
    }

    public boolean eat() {
        if (eaten) {
            return (false);
        }

        eaten = true;
        return (true);
    }

    public boolean isEaten() {
        return (eaten);
    }

    @Override
    public void runStep(int step) {

    }
}
