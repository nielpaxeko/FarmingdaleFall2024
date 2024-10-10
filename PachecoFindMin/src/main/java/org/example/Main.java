package org.example;
import java.util.Random;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int SIZE = 20;
        int MID = 10;
        int[] arr = new int[SIZE];
        Random rand = new Random();

        // Fill the array with random numbers
        for (int i = 0; i < SIZE; i++) {
            arr[i] = rand.nextInt(100) + 1;
        }
        System.out.println("Array: " + Arrays.toString(arr));

        // Create child process and pass the second half of the arr
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "target/classes", "org.example.ChildProcess");
        pb.redirectErrorStream(true);
        for (int i = MID; i < SIZE; i++) {
            pb.command().add(String.valueOf(arr[i]));
        }
        Process process = pb.start();

        // Parent process for first half min
        int parentMin = findMinNum(arr, 0, MID - 1);
        System.out.println("Parent process min: " + parentMin + ", process_ID: " + ProcessHandle.current().pid());

        // Wait for child process to complete and get output which is a currently a string
        int exitCode = process.waitFor();
        String childOutput = new String(process.getInputStream().readAllBytes()).trim();
        System.out.println(childOutput);

        // Find the final and actual minimum in the original array
        // First part is to get the min from the string
        String[] outputParts = childOutput.split(":");
        String[] minAndPid = outputParts[1].trim().split(",");
        int childMin = Integer.parseInt(minAndPid[0].trim()); // Convert to int
        int finalMin = Math.min(parentMin, childMin);
        System.out.println("Final MIN: " + finalMin);
    }

    // Code for finding min
    public static int findMinNum(int[] arr, int startIdx, int endIdx) {
        int min = arr[startIdx];
        for (int i = startIdx + 1; i <= endIdx; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }
}