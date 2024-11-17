package org.example;

public class Thread1 implements Runnable {
    private SharedResource sharedResource;

    public Thread1(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        sharedResource.writeData("I like tacos!");
    }
}
