package ru.lachesis.lesson5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private static final Semaphore semaphore = new Semaphore(MainClass.CARS_COUNT / 2, true);
    private int length;

    public Tunnel(int length) {
        this.length = length;
        this.description = "Тоннель " + length + " метров ";
    }

    @Override
    public void go(Car c) {
//        try {
        try {
            boolean isPrint = true;
            while (true) {
                if (semaphore.tryAcquire()) {
                    System.err.println(c.getName() + " начал этап: " + description + MainClass.timeStamp());
                    Thread.sleep((long) ((float) length / c.getSpeed() * 1000));
                    System.err.println(c.getName() + " закончил этап: " + description + MainClass.timeStamp());
                    break;
                } else if (isPrint) {
                    System.err.println(c.getName() + " готовится к этапу(ждет): " + description + MainClass.timeStamp());
                    isPrint = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

}
