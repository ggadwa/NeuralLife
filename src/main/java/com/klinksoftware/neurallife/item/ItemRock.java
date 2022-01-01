package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Point;
import java.util.Random;

public class ItemRock extends Item {

    public ItemRock(Configuration config, Random random, LifeCanvas lifeCanvas, Board board, Point pnt) {
        super(config, random, lifeCanvas, board, pnt, board.getImage("rock"));
    }

    @Override
    public void runStep(int step) {

    }
}
