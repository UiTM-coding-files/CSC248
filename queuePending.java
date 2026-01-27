import java.util.ArrayList;
import java.util.Scanner;

public class queuePending {
    // Main menu untuk module Pending Task
    public void queuePending(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {
        Scanner in = new Scanner(System.in);
        int opt;
        String queueMenu = """
                        Pending Task Menu
                +----------------------------------+
                | 1. Pending Maintenance List      |
                +----------------------------------+
                | 2. Update Sample(s)              |
                +----------------------------------+
                | 3. Get Suggestion(s)             |
                +----------------------------------+
                | 4. Back to Main Menu             |
                +----------------------------------+
                """;

        do {
            sampleDisplay.clearScreen();
            System.out.print(queueMenu);
            System.out.print("Choose option: ");
            opt = in.nextInt();
            in.nextLine();

            switch (opt) {
                case 1:
                    sampleDisplay.clearScreen();
                    pendingList(normalList, riskQueue);
                    break;

                case 2:
                    sampleDisplay.clearScreen();
                    updateMenu(normalList, riskQueue, in);
                    break;

                case 3:
                    sampleDisplay.clearScreen();
                    suggest(normalList, riskQueue);
                    break;

                case 4:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while (opt != 4);
        sampleDisplay.clearScreen();
    }

    // =============================== Menu untuk pendingList =============================
    private void pendingList(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {
        if (riskQueue.isEmpty()) {
            System.out.println("No pending maintenance samples found.");
            System.out.println("\nPress Enter to continue...");
            new Scanner(System.in).nextLine();
            return;
        }

        System.out.println("Pending Maintenance List (High Risk First, FIFO within each category):\n");
        System.out.println("=".repeat(70));
        System.out.printf("%-10s %-12s %-15s %-20s\n", "Sample ID", "Date", "Risk Level", "Status");
        System.out.println("=".repeat(70));

        // buat temporary list utk hold all samples utk proses
        ArrayList<WaterSample> tempList = new ArrayList<>();
        TaskQueue<WaterSample> tempQueue = new TaskQueue<>();
        
        // transfer pgi templist
        while (!riskQueue.isEmpty()) {
            WaterSample sample = riskQueue.dequeue();
            tempList.add(sample);
            tempQueue.enqueue(sample);
        }
        
        // restore original queue
        while (!tempQueue.isEmpty()) {
            riskQueue.enqueue(tempQueue.dequeue());
        }

        int total = 0;
        int completed = 0;
        int pending = 0;

        // First show HIGH risk samples
        for (WaterSample sample : tempList) {
            if (sample.getRiskLvl().equalsIgnoreCase("High")) {
                total++;
                if (sample.isActionTaken()) {
                    completed++;
                } else {
                    pending++;
                }

                String status = sample.isActionTaken() ? "Action Taken - update in Add sample(s)" : "Needs Action";
                System.out.printf("%-10s %-12s %-15s %-20s\n",
                        sample.getSampleID(),
                        sample.getDate(),
                        sample.getRiskLvl(),
                        status);
            }
        }

        // Then show MODERATE risk samples
        for (WaterSample sample : tempList) {
            if (sample.getRiskLvl().equalsIgnoreCase("Moderate")) {
                total++;
                if (sample.isActionTaken()) {
                    completed++;
                } else {
                    pending++;
                }

                String status = sample.isActionTaken() ? "Action Taken - update in Add sample(s)" : "Needs Action";
                System.out.printf("%-10s %-12s %-15s %-20s\n",
                        sample.getSampleID(),
                        sample.getDate(),
                        sample.getRiskLvl(),
                        status);
            }
        }

        System.out.println("=".repeat(70));
        System.out.println("\nSummary: " + total + " total, " + pending + " pending, " + completed + " completed");
        System.out.println("\nPress Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    // =========================== NEW: Update Menu ===============================
    private void updateMenu(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue, Scanner in) {
        int choice;
        
        do {
            sampleDisplay.clearScreen();
            System.out.println("""
                    Update Samples Menu
                    +----------------------------------+
                    | 1. Update by Sample ID           |
                    +----------------------------------+
                    | 2. Update Highest Priority First |
                    +----------------------------------+
                    | 3. Back to Pending Task Menu     |
                    +----------------------------------+
                    """);
            System.out.print("Choose option: ");
            choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1:
                    updateById(normalList, riskQueue, in);
                    break;
                case 2:
                    updateByPriority(normalList, riskQueue, in);
                    break;
                case 3:
                    System.out.println("Returning to Pending Task Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    System.out.println("Press Enter to continue...");
                    in.nextLine();
            }
        } while (choice != 3);
    }

    // =========================== Menu untuk update by ID ===============================
    private void updateById(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue, Scanner in) {
        System.out.print("Enter Sample ID to mark action taken: ");
        String id = in.nextLine();

        boolean updated = false;

        // Search in risk queue
        TaskQueue<WaterSample> tempQueue = new TaskQueue<>();

        while (!riskQueue.isEmpty()) {
            WaterSample sample = riskQueue.dequeue();
            if (sample.getSampleID().equals(id)) {
                sample.setActionTaken(true);
                updated = true;
            }
            tempQueue.enqueue(sample);
        }

        // Restore the queue
        while (!tempQueue.isEmpty()) {
            riskQueue.enqueue(tempQueue.dequeue());
        }

        // If not found in risk queue, check normal list
        if (!updated) {
            WaterSample ws = normalList.getFirst();
            while (ws != null) {
                if (ws.getSampleID().equals(id)) {
                    ws.setActionTaken(true);
                    updated = true;
                    break;
                }
                ws = normalList.getNext();
            }
        }

        if (updated) {
            WaterSampleManager.writeAllToFile(normalList, riskQueue);
            System.out.println("✓ Sample " + id + " marked as action taken.");
        } else {
            System.out.println("✗ Sample not found.");
        }
        System.out.println("\nPress Enter to continue...");
        in.nextLine();
    }

    // =========================== Menu untuk update by priority ===============================
    private void updateByPriority(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue, Scanner in) {
        if (riskQueue.isEmpty()) {
            System.out.println("No pending samples to update.");
            System.out.println("\nPress Enter to continue...");
            in.nextLine();
            return;
        }

        // Get highest priority samples (not action taken)
        ArrayList<WaterSample> prioritySamples = new ArrayList<>();
        TaskQueue<WaterSample> tempQueue = new TaskQueue<>();
        
        // Collect High risk samples first
        while (!riskQueue.isEmpty()) {
            WaterSample sample = riskQueue.dequeue();
            if (sample.getRiskLvl().equalsIgnoreCase("High") && !sample.isActionTaken()) {
                prioritySamples.add(sample);
            }
            tempQueue.enqueue(sample);
        }
        
        // Restore queue temporarily
        while (!tempQueue.isEmpty()) {
            riskQueue.enqueue(tempQueue.dequeue());
        }
        
        // If no High risk, collect Moderate risk
        if (prioritySamples.isEmpty()) {
            while (!riskQueue.isEmpty()) {
                WaterSample sample = riskQueue.dequeue();
                if (sample.getRiskLvl().equalsIgnoreCase("Moderate") && !sample.isActionTaken()) {
                    prioritySamples.add(sample);
                }
                tempQueue.enqueue(sample);
            }
            
            // Restore queue
            while (!tempQueue.isEmpty()) {
                riskQueue.enqueue(tempQueue.dequeue());
            }
        }

        if (prioritySamples.isEmpty()) {
            System.out.println("No pending samples need action (all are already marked as action taken).");
            System.out.println("\nPress Enter to continue...");
            in.nextLine();
            return;
        }

        // Show the highest priority sample
        WaterSample highestPriority = prioritySamples.get(0);
        sampleDisplay.clearScreen();
        System.out.println("Highest Priority Sample:");
        System.out.println("=".repeat(50));
        System.out.printf("Sample ID: %s\n", highestPriority.getSampleID());
        System.out.printf("Date: %s\n", highestPriority.getDate());
        System.out.printf("Risk Level: %s\n", highestPriority.getRiskLvl());
        
        // Show parameters
        System.out.printf("\nParameters:\n");
        System.out.printf("  Temperature: %.1f °C\n", highestPriority.getTemp());
        System.out.printf("  pH: %.1f\n", highestPriority.getPHlvl());
        System.out.printf("  Ammonia: %.1f\n", highestPriority.getAmmoniaLvl());
        System.out.printf("  Nitrite: %.1f\n", highestPriority.getNitriteLvl());
        System.out.printf("  Nitrate: %.1f\n", highestPriority.getNitrateLvl());
        System.out.println("=".repeat(50));
        
        System.out.print("\nMark this sample as action taken? (Y/N): ");
        String response = in.nextLine().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            // Find and update the sample
            boolean updated = false;
            TaskQueue<WaterSample> updateQueue = new TaskQueue<>();
            
            while (!riskQueue.isEmpty()) {
                WaterSample sample = riskQueue.dequeue();
                if (sample.getSampleID().equals(highestPriority.getSampleID())) {
                    sample.setActionTaken(true);
                    updated = true;
                }
                updateQueue.enqueue(sample);
            }
            
            // Restore the queue
            while (!updateQueue.isEmpty()) {
                riskQueue.enqueue(updateQueue.dequeue());
            }
            
            if (updated) {
                WaterSampleManager.writeAllToFile(normalList, riskQueue);
                System.out.println("\n✓ Sample " + highestPriority.getSampleID() + " marked as action taken.");
            }
        } else {
            System.out.println("\nUpdate cancelled.");
        }
        
        System.out.println("\nPress Enter to continue...");
        in.nextLine();
    }

    // =========================== menu utk suggest ===============================
    private void suggest(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {
        ArrayList<WaterSample> result = new ArrayList<>();

        // Get all samples from risk queue in a list
        ArrayList<WaterSample> riskSamples = new ArrayList<>();
        TaskQueue<WaterSample> tempQueue = new TaskQueue<>();
        
        while (!riskQueue.isEmpty()) {
            WaterSample sample = riskQueue.dequeue();
            riskSamples.add(sample);
            tempQueue.enqueue(sample);
        }
        
        // Restore queue
        while (!tempQueue.isEmpty()) {
            riskQueue.enqueue(tempQueue.dequeue());
        }

        // Add High risk samples first
        for (WaterSample sample : riskSamples) {
            if (sample.getRiskLvl().equalsIgnoreCase("High") && !sample.isActionTaken()) {
                result.add(sample);
            }
        }

        // Then Moderate risk samples
        for (WaterSample sample : riskSamples) {
            if (sample.getRiskLvl().equalsIgnoreCase("Moderate") && !sample.isActionTaken()) {
                result.add(sample);
            }
        }

        // Add normal list samples at the end
        WaterSample s = normalList.getFirst();
        while (s != null) {
            if (!s.isActionTaken()) {
                result.add(s);
            }
            s = normalList.getNext();
        }

        if (result.isEmpty()) {
            System.out.println("No suggestions at this time.");
            System.out.println("\nPress Enter to continue...");
            new Scanner(System.in).nextLine();
        } else {
            sampleDisplay.suggestDisplay(result);
        }
    }
}