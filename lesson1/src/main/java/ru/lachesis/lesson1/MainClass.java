package ru.lachesis.lesson1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainClass {
    public static void main(String[] args) {
        String[] strings = {"aaa", "bbb", "ccc"};
        Integer[] ints = {1, 2, 3, 4};
        System.out.println("-------Задача 1----------");
        System.out.println(Arrays.toString(changeElements(strings)));
        System.out.println(Arrays.toString(changeElements(ints)));
        System.out.println("-------Задача 2----------");

        System.out.println(toArrayList(strings));
        System.out.println(toArrayList(strings).getClass());
        System.out.println(toArrayList(ints));
        System.out.println(toArrayList(ints).getClass());
        System.out.println("-------Задача 3----------");


        Apple a1 = new Apple();
        Fruit a2 = new Apple();
        Orange o1 = new Orange();
        Box<Apple> b1 = new Box<>("b1");
        Box<Orange> b2 = new Box<>("b2");
        Box<Orange> b3 = new Box<>("b3");
        Box<Apple> b4 = new Box<>("b4");

        b1.add(a1);
        b1.getInfo();
        b1.add((Apple) a2);
        b1.getInfo();
//        b1.add(a2); ошибка
//        b1.add(o1)); ошибка
        b2.add(o1);
        b2.getInfo();

        System.out.println("Пытаемся добавить в коробку b2  10 апельсинов");
        for (int i = 0; i < 10; i++) {
            b2.add(o1);
            b2.getInfo();
        }
//        b2.add(a2); ошибка
        System.out.println("-----------------");
        System.out.println("Пытаемся добавить в коробку b1 7 яблок");
        for (int i = 0; i < 7; i++) {
            b1.add(a1);
            b1.getInfo();
        }
        b2.getInfo();
        System.out.println("Результат сравнения b1 и b2: " + b1.compare(b2));
        System.out.println("-----------------");
//        b1.relocate(b2); ошибка
//        b3.add(o1);
        System.out.println("Пытаемся добавить в коробку b3  5 апельсинов");
        for (int i = 0; i < 5; i++)
            b3.add(o1);
        b3.getInfo();
        System.out.println("-----------------");
        b4.getInfo();
        b1.getInfo();
//        b4.add(a1);
//        b4.getInfo();
        System.out.println("b4-->b1");
        b4.relocate(b1);
        b4.getInfo();
        b1.getInfo();
        System.out.println("-----------------");
        System.out.println("b1-->b4");
        b1.relocate(b4);
        b4.getInfo();
        b1.getInfo();
        System.out.println("-----------------");

        b2.getInfo();
        b3.getInfo();
        System.out.println("b2-->b3");
        b2.relocate(b3);
        b2.getInfo();
        b3.getInfo();
    }

    static <T> T[] changeElements(T[] array) {
        int n1, n2;
        Random random = new Random();
        do {
            n1 = random.nextInt(array.length);
            n2 = random.nextInt(array.length);
        } while (n1 == n2);
        T value = array[n1];
        array[n1] = array[n2];
        array[n2] = value;
        System.out.println("Меняем местами элементы № " + n1 + " и " + n2);
        return array;
    }
    ///////////////////////////////////

    static <T> List<T> toArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
}

    ///////////////////////////////////

class Fruit {
    protected float weight;

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }
}

class Apple extends Fruit {
    static final float classWeight = 1F;

    Apple() {
        weight = classWeight;
    }

}

class Orange extends Fruit {
    static final float classWeight = 1.5F;

    Orange() {
        this.weight = classWeight;
    }

}

class Box<T extends Fruit> {
    static final float maxWeight = 10F;
    List<T> boxList;
    Class cls;
    String name;

    public Box(String name) {
        this.boxList = new ArrayList<>();
        this.name = name;
    }

    public void getInfo() {
        System.out.println("Вес коробки " + name + ": " + getWeight());
    }

    public boolean add(T fruit) {
        try {
            cls = fruit.getClass();
            if (isAppend(fruit.getWeight())) {
                boxList.add(fruit);
                return true;
            } else System.out.println("Полна коробочка (вес: " + getWeight() + ")");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAppend(float weight) {
        return maxWeight - getWeight() - weight > 0.001;
    }

    public T getAndRemove() {
        if (boxList.size() != 0) {
            T element = boxList.get(boxList.size() - 1);
            boxList.remove(boxList.size() - 1);
            return element;
        }
        return null;
    }

    public float getWeight() {
        float result = 0;
        if (boxList.size() == 0) {
            return result;
        }
        try {
            float classWeight = getClassWeight();
            result = boxList.size() * classWeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private float getClassWeight() {
        //вычислить класс дженерика через getGenericSuperclass и т.д. - совсем страшная рефлексия, которую и не проходили еще
        // брать первый элемент коллекции, ну, неспортивно же... Хотя, может, в чем-то и хорошо.
        try {
//            System.out.println("вес " + cls.getName() + " " + cls.getDeclaredField("classWeight").get(this));
            return (float) cls.getDeclaredField("classWeight").get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean compare(Box<? extends Fruit> anotherBox) {
        return Math.abs(getWeight() - anotherBox.getWeight()) < 0.001;
    }

    public void relocate(Box<T> anotherBox) {
        T fruit;
        if (getWeight() == 0) {
//            System.out.println("Коробка пуста");
            return;
        }
        while (anotherBox.isAppend(getClassWeight())) {
            try {
                if ((fruit = getAndRemove()) != null) {
                    if (!anotherBox.add(fruit))
                        break;
                } else {
//                    System.out.println("Коробка пуста");
                    break;
                }

            } catch (Exception e) {
                break;
            }
        }
    }
}