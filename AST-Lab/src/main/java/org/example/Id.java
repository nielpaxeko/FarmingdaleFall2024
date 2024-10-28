package org.example;

public class Id extends Expr {
    String name;

    public Id(String name) {
//        super();
        this.name = name;
    }

    @Override
    void show() {
        System.out.println(name);
    }
}
