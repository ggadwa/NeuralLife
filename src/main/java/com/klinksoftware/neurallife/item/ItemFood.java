package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.util.Random;

public class ItemFood extends Item {

    public ItemFood(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board);

        setPoint(board.getRandomPoint());
        setImage("food", 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void runStep(int step) {

    }
}
