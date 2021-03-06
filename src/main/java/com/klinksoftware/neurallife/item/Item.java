package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Item {

    public final static int IMAGE_SIZE = 16;
    public final static int IMAGE_OFFSET = 8;

    private final static float GENERAL_MOVE_SPEED = (float) IMAGE_SIZE;

    protected final Configuration config;
    protected final Random random;
    protected final LifeCanvas lifeCanvas;
    protected final Board board;

    private String imageName;
    private Color sightColor;
    protected Point pnt;
    protected int sightAngle, sightSweep, sightDistance;

    public Item(Configuration config, Random random, LifeCanvas lifeCanvas, Board board, String imageName, Color sightColor) {
        this.config = config;
        this.random = random;
        this.lifeCanvas = lifeCanvas;
        this.board = board;

        this.imageName = imageName;

        this.pnt = new Point(0, 0);

        this.sightAngle = 0;
        this.sightSweep = 0;
        this.sightDistance = 0;
        this.sightColor = sightColor;
    }

    protected final void moveForward(Point vct) {
        pnt.x += vct.x;
        pnt.y += vct.y;
    }

    protected final void moveBackward(Point vct) {
        pnt.x -= vct.x;
        pnt.y -= vct.y;
    }

    protected final Point getMoveDownSightVector() {
        double rad;

        rad = Math.toRadians(sightAngle);
        return (new Point(
                (int) (GENERAL_MOVE_SPEED * Math.cos(rad)),
                -(int) (GENERAL_MOVE_SPEED * Math.sin(rad))
        ));
    }

    protected final boolean isOffLeftEdge() {
        return (pnt.x < IMAGE_OFFSET);
    }

    protected final boolean isOffRightEdge() {
        return (pnt.x > (config.setup.worldWidth - IMAGE_OFFSET));
    }

    protected final boolean isOffTopEdge() {
        return (pnt.y < IMAGE_OFFSET);
    }

    protected final boolean isOffBottomEdge() {
        return (pnt.y > (config.setup.worldHeight - IMAGE_OFFSET));
    }

    public boolean collideWithItemPoint(Point pnt2) {
        if ((pnt.x - IMAGE_OFFSET) > (pnt2.x + IMAGE_OFFSET)) {
            return (false);
        }
        if ((pnt.x + IMAGE_OFFSET) < (pnt2.x - IMAGE_OFFSET)) {
            return (false);
        }
        if ((pnt.y - IMAGE_OFFSET) > (pnt2.y + IMAGE_OFFSET)) {
            return (false);
        }
        return ((pnt.y + IMAGE_OFFSET) >= (pnt2.y - IMAGE_OFFSET));
    }

    public boolean collideWithItem(Item item) {
        return (collideWithItemPoint(item.pnt));
    }

    public boolean pointWithinSight(int x, int y) {
        int dist, ang;
        int startArcAng, endArcAng;

        // within sight distance
        dist = (int) Math.sqrt((Math.pow((double) (x - pnt.x), 2) + Math.pow((double) (y - pnt.y), 2)));
        if (dist > sightDistance) {
            return (false);
        }

        // within sight angle
        ang = (int) Math.toDegrees(Math.atan2((y - pnt.y), (x - pnt.x)));
        ang = (ang < 0) ? (-ang) : (360 - ang);

        startArcAng = sightAngle - (sightSweep / 2);
        if (startArcAng < 0) {
            startArcAng = 360 + startArcAng;
        }

        endArcAng = startArcAng + sightSweep;
        if (endArcAng >= 360) {
            endArcAng -= 360;
        }

        if (startArcAng < endArcAng) {
            return ((ang >= startArcAng) && (ang < endArcAng));
        } else {
            return ((ang > startArcAng) || (ang < endArcAng));
        }
    }

    public boolean itemWithinSight(Item item) {
        // no sight
        if (sightSweep == 0) {
            return (false);
        }

        // check all four points
        if (pointWithinSight((item.pnt.x - IMAGE_OFFSET), item.pnt.y)) {
            return (true);
        }
        if (pointWithinSight((item.pnt.x + IMAGE_OFFSET), item.pnt.y)) {
            return (true);
        }
        if (pointWithinSight(item.pnt.x, (item.pnt.y - IMAGE_OFFSET))) {
            return (true);
        }
        return (pointWithinSight(item.pnt.x, (item.pnt.y + IMAGE_OFFSET)));
    }

    public void backgroundDraw(Graphics2D g) {
        int doubleDistance, startAng;

        // the sight arc
        if (sightSweep != 0) {
            startAng = sightAngle - (sightSweep / 2);
            if (startAng < 0) {
                startAng = 360 + startAng;
            }

            doubleDistance = sightDistance * 2;

            g.setColor(sightColor);
            g.fillArc((pnt.x - sightDistance), (pnt.y - sightDistance), doubleDistance, doubleDistance, startAng, sightSweep);
        }
    }

    public void foregroundDraw(Graphics2D g) {
        BufferedImage img;

        img = board.getImage((board.itemWithinSightOnBoard(this) == null) ? imageName : (imageName + "_highlight"));
        g.drawImage(img, (pnt.x - IMAGE_OFFSET), (pnt.y - IMAGE_OFFSET), lifeCanvas);
    }

    public void runStep(int step) {
    }
}
