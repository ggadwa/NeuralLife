package com.klinksoftware.neurallife.simulation;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.item.Item;
import com.klinksoftware.neurallife.item.ItemFood;
import com.klinksoftware.neurallife.item.ItemMonster;
import com.klinksoftware.neurallife.item.ItemRobot;
import com.klinksoftware.neurallife.item.ItemRock;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;

public class Board {

    private static final String[] IMAGE_NAMES = {"food", "rock", "monster", "robot"};

    private ArrayList<Item> items;
    private int produceFoodNextStep;
    private Font font;
    private HashMap<String, BufferedImage> imgs;
    private final Configuration config;
    private final Random random;
    private final LifeCanvas lifeCanvas;

    public Board(Configuration config, Random random, LifeCanvas lifeCanvas) {
        this.config = config;
        this.random = random;
        this.lifeCanvas = lifeCanvas;

        // no items
        items = new ArrayList<>();

        // misc setup
        font = new Font("Helvetica", Font.PLAIN, 18);

        // images
        imgs = new HashMap<>();

        try {
            for (String name : IMAGE_NAMES) {
                imgs.put(name, ImageIO.read(getClass().getResource("/graphics/" + name + ".png")));
            }
        } catch (IOException e) {
        }
    }

    public BufferedImage getImage(String name) {
        return (imgs.get(name));
    }

    public void startup() {
        int n;
        Point pnt;

        // no items
        items = new ArrayList<>();

        // some specific steps
        produceFoodNextStep = 0;

        // rocks
        for (n = 0; n != config.misc.rockCount; n++) {
            addRock();
        }

        // monsters
        for (n = 0; n != config.monster.count; n++) {
            addMonster();
        }

        // robot
        addRobot(0);

        // initial food
        for (n = 0; n != config.food.initialCount; n++) {
            addFood(0);
        }
    }

    public void draw(Graphics2D g, int step) {
        int x, y;
        int gridSize, gridXCount, gridYCount;
        String stepStr;

        gridSize = config.setup.gridSize;
        gridXCount = config.setup.gridXCount;
        gridYCount = config.setup.gridYCount;

        // black for death edge
        g.fillRect(0, 0, ((gridXCount + 2) * gridSize), ((gridYCount + 2) * gridSize));

        // draw the squares
        for (y = 0; y != gridYCount; y++) {
            for (x = 0; x != gridXCount; x++) {
                g.setColor(((((x + y) & 0x1)) == 0) ? Color.LIGHT_GRAY : Color.WHITE);
                g.fillRect(((x + 1) * gridSize), ((y + 1) * gridSize), gridSize, gridSize);
            }
        }

        // draw the items
        for (Item item : items) {
            item.draw(g);
        }

        // the step
        stepStr = Integer.toString(step);

        g.setColor(Color.BLUE);
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        x = (((gridXCount + 2) * gridSize) - 5) - g.getFontMetrics().stringWidth(stepStr);
        g.drawString(stepStr, x, (gridSize - ((gridSize - 18) / 1)));
    }

    public int countItem(Class c) {
        int count;

        count = 0;

        for (Item item : items) {
            if (item.getClass().isAssignableFrom(c)) {
                count++;
            }
        }

        return (count);
    }

    private Point getRandomSpot() {
        int offset;
        int gridTotal, gridXCount, gridYCount;
        boolean empty, passOnce;
        Point pnt;

        gridXCount = config.setup.gridXCount;
        gridYCount = config.setup.gridYCount;

        gridTotal = gridXCount * gridYCount;
        offset = random.nextInt(gridTotal);

        // create an offset, and then keep adding
        // 1 until we find an empty spot
        pnt = new Point(0, 0);

        passOnce = false;

        while (true) {
            pnt.setLocation((offset % gridXCount), (offset / gridXCount));

            empty = true;
            for (Item item : items) {
                if (item.atPoint(pnt)) {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                return (pnt);
            }

            offset++;
            if (offset >= gridTotal) {
                if (passOnce) {
                    return (null);
                }

                offset = 0;
                passOnce = true;
            }
        }
    }

    private void addRock() {
        Point pnt;

        pnt = getRandomSpot();
        if (pnt == null) {
            System.out.println("step 0: unable to add rock, out of spots");
            return;
        }

        items.add(new ItemRock(config, random, lifeCanvas, this, pnt));
    }

    private void addMonster() {
        Point pnt;

        pnt = getRandomSpot();
        if (pnt == null) {
            System.out.println("step 0: unable to add monster, out of spots");
            return;
        }

        items.add(new ItemMonster(config, random, lifeCanvas, this, pnt));
    }

    private void addRobot(int step) {
        Point pnt;

        pnt = getRandomSpot();
        if (pnt == null) {
            System.out.println("step " + Integer.toString(step) + ": unable to add robot, out of spots");
            return;
        }

        items.add(new ItemRobot(config, random, lifeCanvas, this, pnt));
    }

    private void addFood(int step) {
        Point pnt;

        pnt = getRandomSpot();
        if (pnt == null) {
            System.out.println("step " + Integer.toString(step) + ": unable to add food, out of spots");
            return;
        }

        items.add(new ItemFood(config, random, lifeCanvas, this, pnt));
    }

    private void runStepProduceFood(int step) {
        int foodCount;

        // time for more food?
        if (step < produceFoodNextStep) {
            return;
        }

        produceFoodNextStep = step + config.food.perStepAddRate;

        // is there room?
        foodCount = countItem(ItemFood.class);
        if (foodCount >= config.food.maxCount) {
            return;
        }

        // make the food
        addFood(step);
    }

    public void runStep(int step) {
        runStepProduceFood(step);

        for (Item item : items) {
            item.runStep(step);
        }
    }
}
