package org.example;

public class SharedResource {
    private String data;
    private boolean hasData = false;

    public synchronized void writeData(String newData) {
        data = newData;
        hasData = true;
        System.out.println("Thread_1 updated the data: " + data);
        notify();
    }

    public synchronized void readData() {
       try {
           while (!hasData) {
               wait();
           }
           System.out.println("Thread_2 read the following data: " + data);
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       }
    }
}
