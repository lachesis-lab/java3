package ru.lachesis.lesson6;

import java.util.Arrays;

public class TryJUnit5 {

    int[] getArrayAfterFour(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4)
                return Arrays.copyOfRange(arr, i + 1, arr.length);
        }
        throw new RuntimeException();
    }

    boolean isOneOrFourInArray(int[] arr) {
        int countOne = 0;
        int countFour = 0;
        for (int element : arr) {
            if (element == 1) countOne++;
            else if (element == 4) countFour++;
            else return false;
        }
        return (countOne * countFour != 0);
    }

    boolean isOneOrFourInArrayStream(int[] arr) {

        if (Arrays.stream(arr).distinct().count() == 2)
            return Arrays.stream(arr).allMatch(x -> (x == 1 || x == 4));
        else return false;

    }
}
