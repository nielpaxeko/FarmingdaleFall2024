package org.example;

public class Thread2 implements Runnable {
    private SharedResource sharedResource;

    public Thread2(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        sharedResource.readData();
    }
}

