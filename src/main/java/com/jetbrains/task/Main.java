package com.jetbrains.task;

import com.jetbrains.task.util.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        input = reader.readLine();
        while (!input.equals("exit")) {
            input = reader.readLine();
            Parser parser = new Parser(input);
            if (parser.parse()) {
                System.out.println(parser.getSimplifiedLine());
            }
        }
    }

}
