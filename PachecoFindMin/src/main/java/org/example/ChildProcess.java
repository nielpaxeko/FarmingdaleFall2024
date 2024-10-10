package org.example;

public class ChildProcess {
    public static void main(String[] args) {
        int min = 101; // Array max is never more than 100 so I start with that
        // Simple find min in argument array
        for (int i = 0; i < args.length; i++) {
            int num = Integer.parseInt(args[i]);
            if (num < min) {
                min = num;
            }
        }

        System.out.println("Child process min: " + min  +  ", process_ID: " + ProcessHandle.current().pid());
    }
}