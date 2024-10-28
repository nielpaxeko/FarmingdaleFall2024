package org.example;

import java.io.IOException;
import java.util.Queue;

public class Parser {

    enum TOKEN {ID, PLUS};
    Queue<TOKEN> program;

    public boolean match(TOKEN expectedToken) {
        if (program.peek() == expectedToken) {
            program.remove();
            return true;
        } else {
            return false;
        }
    }

    private Expr factor() {
        if (program.peek() == TOKEN.ID) {
            match(TOKEN.ID);
            return new Id(TOKEN.ID.toString());
        } else {
           return null;
        }
    }

    private Expr exprEnd() {
        Expr left;
        Expr right;
        Expr sum;
        if (program.peek() == TOKEN.PLUS) {
            match(TOKEN.PLUS);
            left = factor();
            right = exprEnd();
            if (right == null) {
                return left;
            }
            sum = new Sum(left, right);
            return sum;
        }
        return null;
    }

    private Expr expr() {
        Expr left;
        Expr right;
        Expr sum;

        left = factor();
        right = exprEnd();

        if (right == null) {
            return left;
        }

        sum = new Sum(left, right);
        return sum;
    }

    public void parse(Queue<TOKEN> program) {
        this.program = program;
        Expr root = expr();
        root.show();
    }
}
