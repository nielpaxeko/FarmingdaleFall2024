package org.example;

// Class for processes
public class Process {
    int processID;
    int burstTime;
    int priority;
    double waitingTime;
    double turnaroundTime;

    public Process(int processID, int burstTime, int priority) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(double turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getProcessID() {
        return processID;
    }

    public double getWaitingTime() {
        return waitingTime;
    }
    public double getTurnaroundTime() {
        return turnaroundTime;
    }
}
