package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Color;
import java.util.Random;

public class ItemDanger extends Item {

    public ItemDanger(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board, "danger", new Color(0.0f, 0.0f, 0.0f));

        pnt = board.getRandomPoint();
    }

    @Override
    public void runStep(int step) {

    }
}
