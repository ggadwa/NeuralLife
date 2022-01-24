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

    private final static Color SIGHT_COLOR = new Color(0.0f, 0.75f, 0.75f);

    private final Configuration config;
    private final Random random;
    private final LifeCanvas lifeCanvas;
    private final Board board;

    private Point pnt;
    private BufferedImage img;
    private int sightAngle, sightSweep, sightDistance;

    public Item(Configuration config, Random random, LifeCanvas lifeCanvas, Board board) {
        this.config = config;
        this.random = random;
        this.lifeCanvas = lifeCanvas;
        this.board = board;

        this.pnt = new Point(0, 0);
        this.img = null;

        this.sightAngle = 0;
        this.sightSweep = 0;
        this.sightDistance = 0;
    }

    protected final void setPoint(Point pnt) {
        this.pnt.x = pnt.x;
        this.pnt.y = pnt.y;
    }

    protected void setImage(BufferedImage img) {
        this.img = img;
    }

    protected final void setupSight(int sweep, int distance) {
        this.sightSweep = sweep;
        this.sightDistance = distance;
    }

    protected final void setSightAngle(int angle) {
        this.sightAngle = angle;
    }

    protected final int getSightAngle() {
        return (sightAngle);
    }

    public boolean collide(Point pnt2) {
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

    public void backgroundDraw(Graphics2D g) {
        int x, y;
        int halfDistance, startAng;

        // the sight arc
        if (sightSweep != 0) {
            halfDistance = sightDistance / 2;
            x = pnt.x - halfDistance;
            y = pnt.y - halfDistance;

            startAng = sightAngle - (sightSweep / 2);
            if (startAng < 0) {
                startAng = 360 + startAng;
            }

            g.setColor(SIGHT_COLOR);
            g.fillArc(x, y, sightDistance, sightDistance, startAng, sightSweep);
        }
    }

    public void foregroundDraw(Graphics2D g) {
        g.drawImage(img, (pnt.x - IMAGE_OFFSET), (pnt.y - IMAGE_OFFSET), lifeCanvas);
    }

    public void runStep(int step) {
    }
}
