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
        setup.gridSize = 25;
        setup.gridXCount = 40;
        setup.gridYCount = 25;

        robot = new ConfigurationRobot();
        robot.initialFuel = 100;
        robot.driveSurvival = 0;
        robot.driveReproduce = 0;
        robot.driveEat = 0;
        robot.driveCuriosity = 0;

        monster = new ConfigurationMonster();
        monster.count = 10;

        food = new ConfigurationFood();
        food.perStepAddRate = 10;
        food.initialCount = 5;
        food.maxCount = 10;
        food.fuelPerFood = 100;

        misc = new ConfigurationMisc();
        misc.rockCount = 40;
    }

    public class ConfigurationSetup {

        public int millisecondPerStep;
        public int gridSize;
        public int gridXCount;
        public int gridYCount;
    }

    public class ConfigurationRobot {

        public int initialFuel;
        public int driveSurvival;
        public int driveReproduce;
        public int driveEat;
        public int driveCuriosity;
    }

    public class ConfigurationMonster {
        public int count;
    }

    public class ConfigurationFood {
        public int perStepAddRate;
        public int initialCount;
        public int maxCount;
        public int fuelPerFood;
    }

    public class ConfigurationMisc {

        public int rockCount;
    }
}
