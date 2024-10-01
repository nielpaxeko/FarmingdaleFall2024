package org.example;


import java.util.Random;

public class Main {

    // Non changing variables
    static final int TOTAL_MEMORY = 16000;
    static final int PAGE_SIZE = 160;
    static final int TOTAL_PAGES = 100;
    static final int START_ADDRESS = 2000;
    static int[] memory = new int[TOTAL_PAGES];

    public static void main(String[] args) {
        for (int i = 0; i < TOTAL_PAGES; i++) {
            memory[i] = -1;
        }
        // Allocate memory for the processes
        userMemoryAllocation();
    }

    // Simple Process Class
    static class Process {
        int process_id;
        int start_address;
        int size;
        int unused_space;


        public Process(int process_id, int start_address, int size, int unused_space) {
            this.process_id = process_id;
            this.start_address = start_address;
            this.size = size;
            this.unused_space = unused_space;
        }
    }

    //
    public static void userMemoryAllocation() {
        int current_address = START_ADDRESS;
        int process_count = 0;
        int current_page = 0; // Starting Page
        Process[] processes = new Process[TOTAL_PAGES];

        while (current_page < TOTAL_PAGES) {
            int process_pages = (int)(Math.random() * 30) + 1;
            int process_size = process_pages * 80;
            // Full page needed at the end as explained in class
            int pages_required = (int) Math.ceil((double) process_size / PAGE_SIZE);

            // Check if there is remaining memory left first
            if (current_page + pages_required > TOTAL_PAGES) {
                System.out.println("There is not enough memory for process " + process_count);
                break;
            } else {
                // Allocated the memory to the new process
                for (int i = 0; i < pages_required; i++) {
                    memory[current_page++] = process_count + 1;
                }
                // Calculate total memory allocated and unused space etc
                int total_memory_allocated = pages_required * PAGE_SIZE;
                int unused_space = total_memory_allocated - process_size;
                processes[process_count] = new Process(process_count + 1, current_address, process_size, unused_space);
                current_address += total_memory_allocated;
                process_count++;
            }

        }
        generateSummaryReport(processes, process_count);

    }

    public static void generateSummaryReport(Process[] memory, int process_count) {
        System.out.println("Summary Report:\n");
        System.out.println("ProcessID\tStart Address\tSize\tUnused pace");
        for (int i = 0; i < process_count; i++) {
            if (memory[i].process_id != -1) {
                // I am using three tabs so that the data looks aligned, but the unused space keeps looking weird no matter what I do
                System.out.println(memory[i].process_id + "\t\t\t" + memory[i].start_address + "\t\t\t" + memory[i].size + "\t\t" + memory[i].unused_space);
            }
        }
    }
}

