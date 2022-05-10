package com.helmes.interview.util;

public class CounterStorage {
    private static int counter = 0;

    public static void increment() {
        counter++;
    }

    public static int get() {
        return counter;
    }
}
