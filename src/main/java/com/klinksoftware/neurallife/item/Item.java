package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Item {

    public final static int IMAGE_SIZE = 16;
    public final static int IMAGE_OFFSET = 8;

    private final Configuration config;
    private final Random random;
    private final LifeCanvas lifeCanvas;
    private final Board board;
    private final Point pnt;
    private final BufferedImage img;

    public Item(Configuration config, Random random, LifeCanvas lifeCanvas, Board board, Point pnt, BufferedImage img) {
        this.config = config;
        this.random = random;
        this.lifeCanvas = lifeCanvas;
        this.board = board;
        this.pnt = (Point) pnt.clone();
        this.img = img;
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

    public void draw(Graphics2D g) {
        g.drawImage(img, (pnt.x - IMAGE_OFFSET), (pnt.y - IMAGE_OFFSET), lifeCanvas);
    }

    public void runStep(int step) {
    }
}
