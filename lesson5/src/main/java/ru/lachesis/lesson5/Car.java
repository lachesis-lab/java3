package ru.lachesis.lesson5;

import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
//    public static final CyclicBarrier start = new CyclicBarrier(MainClass.CARS_COUNT);
    private final Race race;
    private final int speed;
    private final String name;
    private boolean isStarted;
    private boolean isFinished;
    private long result;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT + " (" + speed + "km/h)";
    }

    public void setResult(long result) {
        this.result = result;
    }

    public long getResult() {
        return this.result;
    }

    @Override
    public void run() {
        try {
            System.err.println(this.name + " готовится " + MainClass.timeStamp());
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.err.println(this.name + " готов " + MainClass.timeStamp());
            MainClass.start.countDown();
/*
            if (MainClass.start.getCount()==1) {
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!! " + MainClass.timeStamp());
                MainClass.start.countDown();
            }
*/
            MainClass.start.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);

        }
        MainClass.finish.countDown();
        if (MainClass.finish.getCount() == MainClass.CARS_COUNT - 1) {
            System.err.println(this.name + " WIN: " + MainClass.timeStamp());
        }

    }
}
