package ru.lachesis.lesson6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TryJUnit5Test {

    private final TryJUnit5 tryJUnit = new TryJUnit5();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("createTestArrays1")
    void getArrayAfterFour(int[] originalArray, int[] expectedtArray) {
        int[] actualArray = tryJUnit.getArrayAfterFour(originalArray);
        Assertions.assertArrayEquals(expectedtArray, actualArray);
    }


    @Test
    void getArrayAfterFourThrowException() {
        createTestArrays1();
        int[] originArray1 = new int[]{};
        Assertions.assertThrows(RuntimeException.class, () -> tryJUnit.getArrayAfterFour(originArray1));

        int[] originArray2 = new int[]{1, 2, 3, 5};
        Assertions.assertThrows(RuntimeException.class, () -> tryJUnit.getArrayAfterFour(originArray2));
    }

    @ParameterizedTest
    @MethodSource("createTestArrays2")
    void isOneOrFourInArray(int[] originalArray, boolean expectedResult) {
        boolean actualResult = tryJUnit.isOneOrFourInArray(originalArray);
        assertEquals(expectedResult, actualResult);
    }

    @ParameterizedTest
    @MethodSource("createTestArrays2")
    void isOneOrFourInArrayStream(int[] originalArray, boolean expectedResult) {
        boolean actualResult = tryJUnit.isOneOrFourInArrayStream(originalArray);
        assertEquals(expectedResult, actualResult);
    }

    private static Stream<Arguments> createTestArrays1() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.arguments(new int[]{4, 2, 4, 4, 2, 3, 4, 1, 7}, new int[]{1, 7}));
        args.add(Arguments.arguments(new int[]{4, 2, 4, 4, 2, 3, 4}, new int[]{}));
        args.add(Arguments.arguments(new int[]{4}, new int[]{}));
        return args.stream();
    }

    private static Stream<Arguments> createTestArrays2() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.arguments(new int[]{1, 1, 4, 4, 5}, false));
        args.add(Arguments.arguments(new int[]{1, 1, 4, 1}, true));
        args.add(Arguments.arguments(new int[]{4}, false));
        args.add(Arguments.arguments(new int[]{1, 1}, false));
        args.add(Arguments.arguments(new int[]{}, false));

        return args.stream();
    }

}