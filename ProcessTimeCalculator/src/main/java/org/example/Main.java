package org.example;

import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Process P1 = new Process(1, 2, 2);
        Process P2 = new Process(2, 1, 1);
        Process P3 = new Process(3, 8, 4);
        Process P4 = new Process(4, 4, 2);
        Process P5 = new Process(5, 5, 3);
        Process[] processes = {P1, P2, P3, P4, P5};
        // FCFS
        System.out.println("----------------- FCFS -----------------");
        calculateTimes(processes);
        displayTable(processes);
        // SJF
        System.out.println("----------------- SJF ------------------");
        // Sort array by shortes burst time and calculate again
        Arrays.sort(processes, Comparator.comparingInt(p -> p.burstTime));
        calculateTimes(processes);
        displayTable(processes);

    }

    public static void calculateTimes(Process[] processes) {
        double avgWaitTime = calculateAvgWaitTime(processes);
        System.out.println("Average waiting time is " + avgWaitTime);
        double avgTurnaroundTime = calculateAvgTurnaroundTime(processes);
        System.out.println("Average turnaround time is " + avgTurnaroundTime);
    }

    private static double calculateAvgTurnaroundTime(Process[] processes) {
        double totalTurnaroundTime = 0;

        // Get Turnaround Times
        for (Process process : processes) {
            double processTurnaroundTime = process.waitingTime + process.burstTime;
            totalTurnaroundTime += processTurnaroundTime;
            process.setTurnaroundTime(processTurnaroundTime);
        }
        double avgTurnaroundTime = totalTurnaroundTime / processes.length;
        return avgTurnaroundTime;
    }

    public static double calculateAvgWaitTime(Process[] processes) {
        int totalWaitTime = 0;
        int totalTurnaroundTime = 0;

        // First element wait time is 0
        processes[0].waitingTime = 0;

        // Set other wait times
        for (int i = 1; i < processes.length; i++) {
            // Previous process waiting time + burst time
            double processWaitingTime = processes[i - 1].waitingTime + processes[i - 1].burstTime;
            processes[i].setWaitingTime(processWaitingTime);
            totalWaitTime += processWaitingTime;
        }
        double avgWaitTime = (double) totalWaitTime / processes.length;
        return avgWaitTime;
    }
    public static void displayTable(Process[] processes) {
        System.out.println("Process ID | Waiting Time | Turnaround Time");
        for (Process p : processes) {
            System.out.println(
                    p.getProcessID() + "    |   " + p.getWaitingTime()+ "   |   " + p.getTurnaroundTime()
            );
        }
    }
}

