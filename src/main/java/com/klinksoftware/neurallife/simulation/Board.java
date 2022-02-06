package com.klinksoftware.neurallife.simulation;

import com.klinksoftware.neurallife.LifeCanvas;
import com.klinksoftware.neurallife.item.Item;
import com.klinksoftware.neurallife.item.ItemDanger;
import com.klinksoftware.neurallife.item.ItemFood;
import com.klinksoftware.neurallife.item.ItemMonster;
import com.klinksoftware.neurallife.item.ItemRobot;
import com.klinksoftware.neurallife.item.ItemRock;
import java.awt.Color;
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
    private static final String[] IMAGE_NAMES = {"danger", "food", "monster", "robot", "rock"};
    private static final Color BACKGROUND_COLOR = new Color(0.2f, 1.0f, 0.2f);

    private final Configuration config;
    private final Random random;
    private final LifeCanvas lifeCanvas;

    private ArrayList<Item> items;
    private int produceFoodNextStep;
    private HashMap<String, BufferedImage> imageCache;

    public Board(Configuration config, Random random, LifeCanvas lifeCanvas) {
        this.config = config;
        this.random = random;
        this.lifeCanvas = lifeCanvas;
    }

    public void loadResources() {
        // load images
        imageCache = new HashMap<>();

        for (String imageName : IMAGE_NAMES) {
            try {
                imageCache.put(imageName, ImageIO.read(getClass().getResource("/graphics/" + imageName + ".png")));
                imageCache.put((imageName + "_highlight"), ImageIO.read(getClass().getResource("/graphics/" + imageName + "_highlight.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage getImage(String name) {
        return (imageCache.get(name));
    }

    public void start() {
        int n;
        Point pnt;

        // some specific steps
        produceFoodNextStep = 0;

        // no items
        items = new ArrayList<>();

        // robot
        addRobot(0);

        // rocks
        for (n = 0; n != config.misc.rockCount; n++) {
            addRock();
        }

        // danger
        for (n = 0; n != config.misc.dangerCount; n++) {
            addDanger();
        }

        // monsters
        for (n = 0; n != config.monster.count; n++) {
            addMonster();
        }

        // initial food
        for (n = 0; n != config.food.initialCount; n++) {
            addFood(0);
        }
    }

    public void draw(Graphics2D g, int step) {
        // playing field
        g.setColor(BACKGROUND_COLOR);
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

    public Item collideOnBoard(Item checkItem) {
        for (Item item : items) {
            if (item == checkItem) {
                continue;
            }
            if (checkItem.collideWithItem(item)) {
                return (item);
            }
        }
        return (null);
    }

    public Item itemWithinSightOnBoard(Item checkItem) {
        for (Item item : items) {
            if (item == checkItem) {
                continue;
            }
            if (item.itemWithinSight(checkItem)) {
                return (item);
            }
        }
        return (null);
    }

    public boolean eatFood(ItemFood food) {
        return (food.eat());
    }

    public Item getItem(int index) {
        return (items.get(index));
    }

    public Point getCenterPoint() {
        return (new Point((config.setup.worldWidth / 2), (config.setup.worldHeight / 2)));
    }

    public Point getRandomPoint() {
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
                if (item.collideWithItemPoint(pnt)) {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                return (pnt);
            }
        }
    }

    public Point getRandomPointWithDistanceFromCenter(int dist) {
        Point pnt;
        Point centerPnt;

        centerPnt = getCenterPoint();

        while (true) {
            pnt = getRandomPoint();
            if (pnt.distance(centerPnt) >= dist) {
                return (pnt);
            }
        }
    }

    private void addRock() {
        items.add(new ItemRock(config, random, lifeCanvas, this));
    }

    private void addDanger() {
        items.add(new ItemDanger(config, random, lifeCanvas, this));
    }

    private void addMonster() {
        items.add(new ItemMonster(config, random, lifeCanvas, this));
    }

    private void addRobot(int step) {
        items.add(new ItemRobot(config, random, lifeCanvas, this));
    }

    private void addFood(int step) {
        items.add(new ItemFood(config, random, lifeCanvas, this));
    }

    public void removeItem(Item item) {
        items.remove(item);
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

    public void runStepRemoveFood(int step) {
        int n;
        Item item;

        for (n = (items.size() - 1); n >= 0; n--) {
            item = items.get(n);
            if (item instanceof ItemFood) {
                if (((ItemFood) item).isEaten()) {
                    items.remove(n);
                }
            }
        }
    }

    public void runStep(int step) {
        runStepProduceFood(step);

        for (Item item : items) {
            item.runStep(step);
        }

        runStepRemoveFood(step);
    }
}
