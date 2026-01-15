package com.example;

import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, Commit CI!");
    }

    public int add(int a, int b) {
        return a + b;
    }

    public boolean isYes(String str) {
        return str == "yes!";  // BUG: use "yes".equals(s)
    }

}
