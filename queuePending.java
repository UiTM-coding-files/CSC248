import java.time.LocalDate;
import java.util.Scanner;

public class queuePending {
    //Main menu untuk module Pending Task
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
            System.out.print(queueMenu);
            System.out.print("Choose option: ");
            opt = in.nextInt();
            in.nextLine();

            switch (opt) {
                case 1:
                    pendingList(normalList, riskQueue);
                    break;

                case 2:
                    update(normalList, riskQueue, in);
                    break;

                case 3:
                    suggest(normalList, riskQueue);
                    break;

                case 4:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while (opt != 4);
    }
    //Menu untuk pendingList
    public void pendingList(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue){
        System.out.println("Pending Maintenance List (Moderate/High risk, action not taken):\n");

        SampleLinkedList<WaterSample> list = riskQueue.getList();
        WaterSample ws = list.getFirst();
        boolean found = false;
        while (ws != null) {
            if ((ws.getRiskLvl().equalsIgnoreCase("Moderate") || ws.getRiskLvl().equalsIgnoreCase("High"))
                    && !ws.isActionTaken()) {
                for (String line : ws.toCard()) {
                    System.out.println(line);
                }
                found = true;
            }
            ws = list.getNext();
        }

        if (!found) {
            System.out.println("No pending maintenance samples found.");
        }
        System.out.println();
    }

    //Menu untuk update samples
    public void update(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue, Scanner in){
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
            WaterSample.writeAllToFile(normalList, riskQueue);
            System.out.println("Sample " + id + " marked as action taken.");
        } else {
            System.out.println("Sample not found.");
        }
        System.out.println();
    }

    //Menu untuk AI suggestion
    public void suggest(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue){
        System.out.println("Suggestions for samples added 48+ hours ago:");
        LocalDate cutoff = LocalDate.now().minusDays(2);

        // check both lists
        SampleLinkedList<WaterSample> lists[] = new SampleLinkedList[] { riskQueue.getList(), normalList };

        boolean found = false;
        for (SampleLinkedList<WaterSample> l : lists) {
            WaterSample s = l.getFirst();
            while (s != null) {
                if ((s.getDate().isBefore(cutoff) || s.getDate().isEqual(cutoff)) && !s.isActionTaken()) {
                    System.out.println("ID: " + s.getSampleID() + " | Risk: " + s.getRiskLvl() + " | Date: " + s.getDate());
                    if (s.getRiskLvl().equalsIgnoreCase("High")) {
                        System.out.println(" Suggestion: Immediate action required (notify team, retest, treat).\n");
                    } else if (s.getRiskLvl().equalsIgnoreCase("Moderate")) {
                        System.out.println(" Suggestion: Re-test within 24 hours / monitor closely.\n");
                    } else {
                        System.out.println(" Suggestion: No urgent action but monitor.\n");
                    }
                    found = true;
                }
                s = l.getNext();
            }
        }

        if (!found) {
            System.out.println("No suggestions at this time.");
        }
        System.out.println();
    }
}
