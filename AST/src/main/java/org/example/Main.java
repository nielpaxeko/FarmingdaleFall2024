package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MyParser parser = new MyParser();
        String program = "declare w\n" +
                "declare x\n" +
                "declare y\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "calc w = x + y + 4\n" +
                "if x = y then\n" +
                "print w\n" +
                "print x\n" +
                "endif";

        try {
            if (parser.parse(program)) {
                System.out.println("Parsing successful!");
                parser.getAst().display();
            } else {
                System.out.println("Parsing failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}