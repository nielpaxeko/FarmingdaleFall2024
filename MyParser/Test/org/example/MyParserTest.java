package org.example;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MyParserTest {

    // This test is for the correct program
    // I recommend to test each test indiviually
    @Test
    public void test1() throws IOException {
        MyParser parser = new MyParser();
        String program = "declare w int\n" +
                "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "set z = 20\n" +
                "calc w = x + y + z\n" +
                "if x = y then\n" +
                "print w\n" +
                "print x\n" +
                "endif\n" +
                "print y\n" +
                "print z\n";
        assertTrue(parser.parse(program));
    }

    @Test
    public void test2() throws IOException {
        MyParser parser = new MyParser();
        String program = "declare w int\n" +
                "declare x int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                " if w x then\n" +
                " print w\n" +
                " endif\n";

        assertTrue(parser.parse(program));
        // This is the error message I got after running: Error: Expected EQUALS but got ID instead.
    }

    @Test
    public void test3() throws IOException {
        // For the following test technically both w, x and y are undeclared so I get an error at the beginning.
        // I decided to make my program check for undeclared regardless
        MyParser parser = new MyParser();
        String program = "set w = 5\n" +
                "set x = 10\n" +
                "calc y = w + x";
        assertTrue(parser.parse(program));
    }

    // Extra test
    @Test
    public void test4() throws IOException {
        // Declared w and x in this test to test for y
        // As expected it fails because y is also undeclared
        MyParser parser = new MyParser();
        String program =  "declare w int\n" +
                "declare x int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "calc y = w + x";
        assertTrue(parser.parse(program));
    }


}