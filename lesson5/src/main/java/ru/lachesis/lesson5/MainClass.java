package ru.lachesis.lesson5;

import java.util.concurrent.*;


public class MainClass {
    //    Практическое задание
//    Перенести приведенный ниже код в новый проект, где мы организуем гонки.
//    Все участники должны стартовать одновременно, несмотря на разное время  подготовки. В тоннель не может одновременно заехать больше половины участников (условность).
//    Попробуйте все это синхронизировать.
//    Первый участник, пересекший финишную черту, объявляется победителем (в момент пересечения этой самой черты). Победитель должен быть только один (ситуация с 0 или 2+ победителями недопустима).
//    Когда все завершат гонку, нужно выдать объявление об окончании.
//    Можно корректировать классы (в том числе конструктор машин) и добавлять объекты классов из пакета java.util.concurrent.
    public static final int CARS_COUNT = 4;
    public static final long startTime = System.currentTimeMillis();
    public static CountDownLatch start = new CountDownLatch(MainClass.CARS_COUNT+1);
    public static CountDownLatch finish = new CountDownLatch(MainClass.CARS_COUNT+1);
//    public static CyclicBarrier start = new CyclicBarrier(MainClass.CARS_COUNT);
//    public static boolean isStart=false;
    static {
        System.out.println("Начало отсчета: "+timeStamp());
    }
    static String timeStamp(){
        return System.currentTimeMillis()-startTime+"ms";
    }
    static long lTimeStamp(){
        return System.currentTimeMillis()-startTime;
    }

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!! "+timeStamp());
        Race race = new Race(new Road(100),new Tunnel(200), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        finish.countDown();

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 100));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        while (start.getCount() >1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!! " + timeStamp());
        start.countDown();

        while (finish.getCount() >0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}


