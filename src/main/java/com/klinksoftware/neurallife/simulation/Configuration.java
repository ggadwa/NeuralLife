package com.klinksoftware.neurallife.simulation;

public class Configuration {
    public ConfigurationSetup setup;
    public ConfigurationRobot robot;
    public ConfigurationMonster monster;
    public ConfigurationFood food;
    public ConfigurationMisc misc;

    public Configuration() {

        setup = new ConfigurationSetup();
        setup.millisecondPerStep = 1000;
        setup.worldWidth = 1000;
        setup.worldHeight = 600;

        robot = new ConfigurationRobot();
        robot.initialFuelCount = 100;
        robot.reproduceFuelCount = 150;
        robot.sightMaxTurn = 45;
        robot.sightSweep = 45;
        robot.sightDistance = 200;
        robot.driveSurvival = 0;
        robot.driveReproduce = 0;
        robot.driveEat = 0;
        robot.driveCuriosity = 0;

        monster = new ConfigurationMonster();
        monster.count = 10;
        monster.minStartDistanceFromRobot = 250;
        monster.sightMaxTurn = 45;
        monster.sightSweep = 45;
        monster.sightDistance = 200;
        monster.maxChaseStepCount = 10;
        monster.chaseCoolDownStepCount = 5;

        food = new ConfigurationFood();
        food.perStepAddRate = 10;
        food.initialCount = 5;
        food.maxCount = 10;
        food.fuelPerFood = 100;

        misc = new ConfigurationMisc();
        misc.rockCount = 40;
        misc.dangerCount = 10;
    }

    public class ConfigurationSetup {
        public int millisecondPerStep;
        public int worldWidth;
        public int worldHeight;
    }

    public class ConfigurationRobot {
        public int initialFuelCount;
        public int reproduceFuelCount;
        public int sightMaxTurn;
        public int sightSweep;
        public int sightDistance;
        public int driveSurvival;
        public int driveReproduce;
        public int driveEat;
        public int driveCuriosity;
    }

    public class ConfigurationMonster {
        public int count;
        public int minStartDistanceFromRobot;
        public int sightMaxTurn;
        public int sightSweep;
        public int sightDistance;
        public int maxChaseStepCount;
        public int chaseCoolDownStepCount;
    }

    public class ConfigurationFood {
        public int perStepAddRate;
        public int initialCount;
        public int maxCount;
        public int fuelPerFood;
    }

    public class ConfigurationMisc {
        public int rockCount;
        public int dangerCount;
    }
}
