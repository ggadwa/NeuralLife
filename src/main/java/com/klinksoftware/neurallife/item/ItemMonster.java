package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Point;
import java.util.Random;

public class ItemMonster extends Item {

    private final static int STATE_TURN = 0;
    private final static int STATE_MOVE = 1;
    private final static int STATE_CHASE = 2;

    private int state;
    private int stepCount;

    public ItemMonster(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board);

        setPoint(board.getRandomPointWithDistanceFromCenter(config.monster.minStartDistanceFromRobot));
        setImage("monster", 0.8f, 0.2f, 0.2f);
        setupSight(config.monster.sightSweep, config.monster.sightDistance);
        setSightAngle(random.nextInt(360));

        state = STATE_TURN;
    }

    private void runStepTurn(int step) {
        int turn;
        Configuration config = getConfig();
        Random random = getRandom();

        turn = (int) ((float) config.monster.sightMaxTurn * (random.nextFloat(2.0f) - 1.0f));
        setSightAngle(getSightAngle() + turn);

        state = STATE_MOVE;
        stepCount = (int) ((float) config.monster.maxMoveStepCount * random.nextFloat());
    }

    private void runStepMove(int step) {
        Point vct;

        vct = getMoveDownSightVector();
        moveForward(vct);

        // off the screen, turn around and
        // go back to a random walk
        if (isOffLeftEdge()) {
            moveBackward(vct);
            setSightAngle(0);
            runStepTurn(step);
            return;
        }
        if (isOffRightEdge()) {
            moveBackward(vct);
            setSightAngle(180);
            runStepTurn(step);
            return;
        }
        if (isOffTopEdge()) {
            moveBackward(vct);
            setSightAngle(270);
            runStepTurn(step);
            return;
        }
        if (isOffBottomEdge()) {
            moveBackward(vct);
            setSightAngle(90);
            runStepTurn(step);
            return;
        }

        // next step count
        stepCount--;
        if (stepCount <= 0) {
            state = STATE_TURN;
        }
    }

    private void runStepChase(int step) {

    }

    @Override
    public void runStep(int step) {
        switch (state) {
            case STATE_TURN:
                runStepTurn(step);
                break;
            case STATE_MOVE:
                runStepMove(step);
                break;
            case STATE_CHASE:
                runStepChase(step);
                break;
        }
        setSightAngle((getSightAngle() + 1) % 360);
    }
}
