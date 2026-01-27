
import java.util.ArrayList;
import java.util.Scanner;

public class queuePending {
    private static sampleDisplay sampleD = new sampleDisplay();

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
            sampleD.clearScreen();
            System.out.print(queueMenu);
            System.out.print("Choose option: ");
            opt = in.nextInt();
            in.nextLine();

            switch (opt) {
                case 1:
                    sampleD.clearScreen();
                    pendingList(normalList, riskQueue);
                    break;

                case 2:
                    sampleD.clearScreen();
                    update(normalList, riskQueue, in);
                    break;

                case 3:
                    sampleD.clearScreen();
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

    // Menu untuk pendingList
    public void pendingList(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {
        ArrayList<WaterSample> filteredSamples = new ArrayList<>();

        SampleLinkedList<WaterSample> list = riskQueue.getList();
        WaterSample ws = list.getFirst();

        while (ws != null) {
            // Show ALL risk samples (both pending and completed)
            if (ws.getRiskLvl().equalsIgnoreCase("Moderate") ||
                    ws.getRiskLvl().equalsIgnoreCase("High")) {
                filteredSamples.add(ws);
            }
            ws = list.getNext();
        }

        if (filteredSamples.isEmpty()) {
            System.out.println("No pending maintenance samples found.");
        } else {
            // Display with status
            System.out.println("Pending Maintenance List:\n");
            System.out.println("=".repeat(70));
            System.out.printf("%-10s %-12s %-15s %-20s\n", "Sample ID", "Date", "Risk Level", "Status");
            System.out.println("=".repeat(70));

            for (WaterSample sample : filteredSamples) {
                String status = sample.isActionTaken() ? "✓ Action Taken" : "⚠ Needs Action";
                System.out.printf("%-10s %-12s %-15s %-20s\n",
                        sample.getSampleID(),
                        sample.getDate(),
                        sample.getRiskLvl(),
                        status);
            }
            System.out.println("=".repeat(70));

            // Count statistics
            int total = filteredSamples.size();
            int completed = 0;
            for (WaterSample sample : filteredSamples) {
                if (sample.isActionTaken())
                    completed++;
            }
            int pending = total - completed;

            System.out.println("\nSummary: " + total + " total, " + pending + " pending, " + completed + " completed");
            System.out.println("\nPress Enter to continue...");
            Scanner temp = new Scanner(System.in);
            temp.nextLine();
        }
    }

    // Menu untuk update samples
    public void update(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue, Scanner in) {
        System.out.print("Enter Sample ID to mark action taken: ");
        String id = in.nextLine();

        // search in risk queue list
        SampleLinkedList<WaterSample> list = riskQueue.getList();
        WaterSample ws = list.getFirst();
        boolean updated = false;
        while (ws != null) {
            if (ws.getSampleID().equals(id)) {
                ws.setActionTaken(true);
                updated = true;
                break;
            }
            ws = list.getNext();
        }

        // also check normal list just in case
        if (!updated) {
            ws = normalList.getFirst();
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
            System.out.println("Sample " + id + " marked as action taken.");
        } else {
            System.out.println("Sample not found.");
        }
        System.out.println();
    }

    public void suggest(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {
        ArrayList<WaterSample> result = new ArrayList<>();
        SampleLinkedList<WaterSample>[] lists = new SampleLinkedList[] { riskQueue.getList(), normalList };

        for (SampleLinkedList<WaterSample> l : lists) {
            WaterSample s = l.getFirst();
            while (s != null) {
                if (!s.isActionTaken()) { // <-- Only add if action NOT taken
                    result.add(s);
                }
                s = l.getNext();
            }
        }

        if (result.isEmpty()) {
            System.out.println("No suggestions at this time.");
        } else {
            sampleDisplay.suggestDisplay(result);
        }
    }
}
