package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class ItemMonster extends Item {

    private final static int STATE_TURN = 0;
    private final static int STATE_MOVE = 1;
    private final static int STATE_CHASE = 2;

    private int state;
    private int stepCount;

    public ItemMonster(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        super(config, random, lifeCanvas, board, "monster", new Color(0.8f, 0.2f, 0.2f));

        pnt = board.getRandomPointWithDistanceFromCenter(config.monster.minStartDistanceFromRobot);
        sightAngle = random.nextInt(360);
        sightSweep = config.monster.sightSweep;
        sightDistance = config.monster.sightDistance;

        state = STATE_TURN;
    }

    private void runStepTurn(int step) {
        int turn;
        turn = (int) ((float) config.monster.sightMaxTurn * (random.nextFloat(2.0f) - 1.0f));
        sightAngle = (sightAngle + turn) % 360;

        state = STATE_MOVE;
        stepCount = (int) ((float) config.monster.maxMoveStepCount * random.nextFloat());
    }

    private void runStepMove(int step) {
        int x, y;
        Point vct;
        Point vct2;
        Item collideItem;

        vct = getMoveDownSightVector();
        moveForward(vct);

        // off the screen, turn around and
        // go back to a random walk
        if (isOffLeftEdge()) {
            moveBackward(vct);
            sightAngle = 0;
            runStepTurn(step);
            return;
        }
        if (isOffRightEdge()) {
            moveBackward(vct);
            sightAngle = 180;
            runStepTurn(step);
            return;
        }
        if (isOffTopEdge()) {
            moveBackward(vct);
            sightAngle = 270;
            runStepTurn(step);
            return;
        }
        if (isOffBottomEdge()) {
            moveBackward(vct);
            sightAngle = 90;
            runStepTurn(step);
            return;
        }

        // collide with other item
        collideItem = board.collideOnBoard(this);
        if (collideItem != null) {

            // touching player
            // touching a hazzard immediately backs off
            if (collideItem instanceof ItemDanger) {
                moveBackward(vct);
                sightAngle = (sightAngle + 180) % 360;
                runStepTurn(step);
                return;
            }

            // touching a food eats it
            if (collideItem instanceof ItemFood) {
                if (board.eatFood((ItemFood) collideItem)) { // if false, food already eaten on this step
                    collideItem = null;
                }
            }
        }

        // sliding
        if (collideItem != null) {
            x = pnt.x;
            y = pnt.y;

            // try x only
            moveBackward(vct);
            vct2 = new Point(vct.x, 0);
            moveForward(vct2);
            if (board.collideOnBoard(this) != null) {
                moveBackward(vct2);
            }

            // try z only
            vct2 = new Point(0, vct.y);
            moveForward(vct2);
            if (board.collideOnBoard(this) != null) {
                moveBackward(vct2);
            }

            // no movement at all, back out
            if ((pnt.x == x) && (pnt.y == y)) {
                sightAngle = (sightAngle + 180) % 360;
                runStepTurn(step);
                return;
            }
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
    }
}
