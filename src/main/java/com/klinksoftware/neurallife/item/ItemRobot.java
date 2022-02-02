package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Color;
import java.util.Random;

public class ItemRobot extends Item {

    public ItemRobot(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board, "robot", new Color(0.2f, 0.2f, 0.8f));

        pnt = board.getCenterPoint();
        sightAngle = random.nextInt(360);
        sightSweep = config.robot.sightSweep;
        sightDistance = config.robot.sightDistance;
    }

    @Override
    public void runStep(int step) {

    }
}
