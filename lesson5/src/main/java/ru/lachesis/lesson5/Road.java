package ru.lachesis.lesson5;

public class Road extends Stage {
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров ";
    }

    @Override
    public void go(Car c) {
        try {
            System.err.println(c.getName() + " начал этап: " + description + MainClass.timeStamp());
            Thread.sleep((long) ((float) length / c.getSpeed() * 1000));
            c.setResult(MainClass.lTimeStamp());
            System.err.println(c.getName() + " закончил этап: " + description + MainClass.timeStamp());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
