package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // I made three class, the shared resource class, the thread1 class and the thread 2 class
        SharedResource sharedResource = new SharedResource();
        // Create and start the threads
        Thread thread1 = new Thread(new Thread1(sharedResource), "Thread_1");
        Thread thread2 = new Thread(new Thread2(sharedResource), "Thread_2");
        thread1.start();
        thread2.start();
    }
}