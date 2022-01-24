package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.util.Random;

public class ItemMonster extends Item {

    public ItemMonster(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board);

        setPoint(board.getRandomPointWithDistanceFromCenter(config.monster.minStartDistanceFromRobot));
        setImage(board.getImage("monster"));
        setupSight(config.monster.sightSweep, config.monster.sightDistance);
        setSightAngle(random.nextInt(360));
    }

    @Override
    public void runStep(int step) {
        setSightAngle((getSightAngle() + 1) % 360);
    }
}
