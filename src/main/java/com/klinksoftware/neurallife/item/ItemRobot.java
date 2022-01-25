package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.util.Random;

public class ItemRobot extends Item {

    public ItemRobot(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board);

        setPoint(board.getCenterPoint());
        setImage("robot");
        setupSight(config.robot.sightSweep, config.robot.sightDistance);
        setSightAngle(random.nextInt(360));
    }

    @Override
    public void runStep(int step) {

    }
}
