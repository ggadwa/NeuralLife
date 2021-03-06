package com.klinksoftware.neurallife;

public class NeuralLife {

    private static final Object mainLock;
    public static final AppWindow appWindow;

    static {
        mainLock = new Object();
        appWindow = new AppWindow();
    }

    public static void stop() {
        synchronized (mainLock) {
            mainLock.notify();
        }
    }

    public static void main(String[] args) {
        // start the window
        appWindow.start();

        // lock the main thread until it's time
        // to quit
        synchronized (mainLock) {
            try {
                mainLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        appWindow.stop();

        System.exit(0);
    }

}
