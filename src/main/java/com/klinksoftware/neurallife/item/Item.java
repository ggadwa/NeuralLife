package com.klinksoftware.neurallife.item;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.simulation.Board;
import com.klinksoftware.neurallife.simulation.Configuration;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Item {

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

    public boolean atPoint(Point pnt) {
        return (this.pnt.equals(pnt));
    }

    public void draw(Graphics2D g) {
        int dx, dy;

        dx = ((pnt.x + 1) * config.setup.gridSize) + ((config.setup.gridSize - img.getWidth(null)) / 2);
        dy = ((pnt.y + 1) * config.setup.gridSize) + ((config.setup.gridSize - img.getHeight(null)) / 2);

        g.drawImage(img, dx, dy, lifeCanvas);
    }

    public void runStep(int step) {
    }
}
