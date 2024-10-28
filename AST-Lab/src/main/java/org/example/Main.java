package org.example;

import java.util.LinkedList;
import java.util.Queue;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Queue<Parser.TOKEN> program = new LinkedList<>();
        program.add(Parser.TOKEN.ID);
        program.add(Parser.TOKEN.PLUS);
        program.add(Parser.TOKEN.ID);
        program.add(Parser.TOKEN.PLUS);
        program.add(Parser.TOKEN.ID);
        Parser parser = new Parser();
        parser.parse(program);

    }
}