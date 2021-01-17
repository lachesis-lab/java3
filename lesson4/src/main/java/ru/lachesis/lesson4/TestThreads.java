package ru.lachesis.lesson4;

import java.util.ArrayList;


public class TestThreads {
    final Object mon = new Object();
    ArrayList<String> letters = new ArrayList<>();
    ArrayList<Thread> threads = new ArrayList<>();
    volatile int currentIndex;
    int count = 5;

    private TestThreads() {
        letters.add("A");
        letters.add("B");
        letters.add("C");
        letters.add("D");
        letters.add(" ");
        currentIndex = 0;
    }

    private void printLetter(int index) {
        synchronized (mon) {
            try {
                for (int i = 0; i < count; i++) {
                    while (index != currentIndex) {
                        mon.wait();
                    }
                    System.out.print(letters.get(currentIndex));

                    if (index < letters.size() - 1)
                        currentIndex++;
                    else {
                        currentIndex = 0;
//                        System.out.print(" ");
                    }
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TestThreads tt = new TestThreads();
        for (int i = 0; i < tt.letters.size(); i++) {
            int ii = i;
            tt.threads.add(new Thread(() -> tt.printLetter(ii)));
            tt.threads.get(i).start();
        }

    }

}

