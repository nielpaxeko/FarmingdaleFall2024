package org.example;

public class Sum extends Expr {
    Expr left;
    Expr right;

    public Sum(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    void show() {
        System.out.println("SUM");
        System.out.println("LHS");
        left.show();
        System.out.println("RHS");
        right.show();
    }
}
