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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;

public class Board {

    public static final int STATUS_BAR_HEIGHT = 30;

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
        // playing field
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, config.setup.worldWidth, config.setup.worldHeight);

        // item backgrounds
        for (Item item : items) {
            item.backgroundDraw(g);
        }

        // item foregrounds
        for (Item item : items) {
            item.foregroundDraw(g);
        }
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
        int randomWidth, randomHeight;
        boolean empty;
        Point pnt;

        randomWidth = config.setup.worldWidth - Item.IMAGE_SIZE;
        randomHeight = config.setup.worldHeight - Item.IMAGE_SIZE;

        pnt = new Point(0, 0);

        while (true) {
            pnt.setLocation((random.nextInt(randomWidth) + Item.IMAGE_OFFSET), (random.nextInt(randomHeight) + Item.IMAGE_OFFSET));

            empty = true;
            for (Item item : items) {
                if (item.collide(pnt)) {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                return (pnt);
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
