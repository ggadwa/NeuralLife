package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;

public class Item {

    public final static int IMAGE_SIZE = 16;
    public final static int IMAGE_OFFSET = 8;

    private final static float GENERAL_MOVE_SPEED = (float) IMAGE_SIZE;

    private final static HashMap<String, BufferedImage> imageCache;

    private final Configuration config;
    private final Random random;
    private final LifeCanvas lifeCanvas;
    private final Board board;

    private Point pnt;
    private BufferedImage img;
    private int sightAngle, sightSweep, sightDistance;
    private Color sightColor;

    static {
        imageCache = new HashMap<>();
    }

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
        this.sightColor = null;
    }

    protected Configuration getConfig() {
        return (config);
    }

    protected Random getRandom() {
        return (random);
    }

    protected final void setPoint(Point pnt) {
        this.pnt.x = pnt.x;
        this.pnt.y = pnt.y;
    }

    protected void setImage(String name, float sightRed, float sightGreen, float sightBlue) {
        img = imageCache.get(name);
        if (img == null) {

            try {
                img = ImageIO.read(getClass().getResource("/graphics/" + name + ".png"));
            } catch (IOException e) {
                return; // leave image null
            }

            imageCache.put(name, img);
        }

        sightColor = new Color(sightRed, sightGreen, sightBlue);
    }

    protected final void setupSight(int sweep, int distance) {
        sightSweep = sweep;
        sightDistance = distance;
    }

    protected final void setSightAngle(int angle) {
        sightAngle = angle % 360;
    }

    protected final int getSightAngle() {
        return (sightAngle);
    }

    protected final boolean moveDownSight() {
        double rad;
        boolean edgeFail;

        rad = Math.toRadians(sightAngle);
        pnt.x += (GENERAL_MOVE_SPEED * Math.cos(rad));
        pnt.y -= (GENERAL_MOVE_SPEED * Math.sin(rad));

        edgeFail = false;

        if (pnt.x < IMAGE_OFFSET) {
            pnt.x = IMAGE_OFFSET;
            edgeFail = true;
        } else {
            if (pnt.x > (config.setup.worldWidth - IMAGE_OFFSET)) {
                pnt.x = (config.setup.worldWidth - IMAGE_OFFSET) - 1;
                edgeFail = true;
            }
        }

        if (pnt.y < IMAGE_OFFSET) {
            pnt.y = IMAGE_OFFSET;
            edgeFail = true;
        } else {
            if (pnt.y > (config.setup.worldHeight - IMAGE_OFFSET)) {
                pnt.y = (config.setup.worldHeight - IMAGE_OFFSET) - 1;
                edgeFail = true;
            }
        }

        return (edgeFail);
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

            g.setColor(sightColor);
            g.fillArc(x, y, sightDistance, sightDistance, startAng, sightSweep);
        }
    }

    public void foregroundDraw(Graphics2D g) {
        g.drawImage(img, (pnt.x - IMAGE_OFFSET), (pnt.y - IMAGE_OFFSET), lifeCanvas);
    }

    public void runStep(int step) {
    }
}
