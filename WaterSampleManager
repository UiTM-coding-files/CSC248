import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class WaterSampleManager {
    private static final String FILE_NAME = "Samples.txt";
    private static sampleDisplay sampleD = new sampleDisplay();

    // ------------------------ MAIN SAMPLE MENU ------------------------
    public static void manageSamples(SampleLinkedList<WaterSample> normalList,
                                    TaskQueue<WaterSample> riskQueue) {
        Scanner input = new Scanner(System.in);
        int opt;

        do {
            String sampleMenu = """
                        Add Sample Menu
                    +------------------------+
                    | 1. Add new sample      |
                    +------------------------+
                    | 2. Search sample(s)    |
                    +------------------------+
                    | 3. Remove samples(s)   |
                    +------------------------+
                    | 4. Back to Main Menu   |
                    +------------------------+
                    """;
            System.out.println(sampleMenu);
            System.out.print("Choose Option: ");
            opt = input.nextInt();
            input.nextLine();

            switch (opt) {
                case 1 -> {
                    sampleD.clearScreen();
                    addSample(input, normalList, riskQueue);
                }
                case 2 -> {
                    sampleD.clearScreen();
                    System.out.print("Enter sample ID to Search: ");
                    String key = input.nextLine();
                    searchAndEdit(key, normalList, riskQueue, input);
                }
                case 3 -> {
                    sampleD.clearScreen();
                    System.out.print("Enter sample ID to Remove: ");
                    String id = input.nextLine();
                    removeSample(id, normalList, riskQueue);
                }
                case 4 -> sampleD.clearScreen();
                default -> System.out.println("Invalid Option. Try Again.");
            }
        } while (opt != 4);
    }

    // ------------------------ AUTO ID GENERATION ------------------------
    private static String generateID() {
        String lastLine = null;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException ignored) {
        }

        if (lastLine == null) {
            return "001";
        }

        int lastID = Integer.parseInt(lastLine.split(",")[0]);
        return String.format("%03d", lastID + 1);
    }

    // ------------------------ ADD SAMPLE ------------------------
    public static void addSample(Scanner input,
                                SampleLinkedList<WaterSample> normalList,
                                TaskQueue<WaterSample> riskQueue) {

        String id = generateID();
        double temp = readDouble(input, "Temperature: ");
        double pH = readDouble(input, "pH Level: ");
        double ammonia = readDouble(input, "Ammonia Level: ");
        double nitrite = readDouble(input, "Nitrite Level: ");
        double nitrate = readDouble(input, "Nitrate Level: ");
        double alk = readDouble(input, "Alkalinity Level: ");
        double gh = readDouble(input, "General Hardness: ");
        LocalDate date = readDate(input, "Date [YYYY-MM-DD]: ");

        WaterSample ws = new WaterSample(id, temp, pH, ammonia, nitrite, nitrate, alk, gh, date);

        if (ws.getRiskLvl().equalsIgnoreCase("Normal")) {
            normalList.addLast(ws);
        } else {
            riskQueue.enqueue(ws);
        }

        writeAllToFile(normalList, riskQueue);

        System.out.println("[ ID: " + id + ", Risk Level: " + ws.getRiskLvl() + " ]");
        System.out.println("Sample added successfully.\n");
    }

    // ------------------------ SEARCH AND EDIT ------------------------
    public static void searchAndEdit(String key, SampleLinkedList<WaterSample> normalList,
                                    TaskQueue<WaterSample> riskQueue, Scanner input) {

        // Search in Normal List
        WaterSample ws = normalList.getFirst();
        while (ws != null) {
            if (ws.getSampleID().equals(key)) {
                displayAndEditSample(ws, input, normalList, riskQueue);
                return;
            }
            ws = normalList.getNext();
        }

        // Search in Risk Queue
        SampleLinkedList<WaterSample> q = riskQueue.getList();
        ws = q.getFirst();
        while (ws != null) {
            if (ws.getSampleID().equals(key)) {
                displayAndEditSample(ws, input, normalList, riskQueue);
                return;
            }
            ws = q.getNext();
        }

        System.out.println("Sample Not Found.\n");
    }

    private static void displayAndEditSample(WaterSample ws, Scanner input,
                                           SampleLinkedList<WaterSample> normalList,
                                           TaskQueue<WaterSample> riskQueue) {
        System.out.println("Sample Found: ");
        for (String line : ws.toCard()) {
            System.out.println(line);
        }

        System.out.print("Do you want to edit this sample? [Y/N]: ");
        String choice = input.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            editSample(ws, input);
            writeAllToFile(normalList, riskQueue);
            sampleDisplay.clearScreen();
            System.out.println("Sample Updated.");
        } else {
            sampleDisplay.clearScreen();
            System.out.println("Edit cancelled.");
        }
    }

    // ------------------------ EDIT SAMPLE ------------------------
    private static void editSample(WaterSample ws, Scanner input) {
        int choice;

        System.out.println("""
                +-------------------------+
                | 1. Edit Temperature     |
                | 2. Edit pH Level        |
                | 3. Edit Ammonia Level   |
                | 4. Edit Nitrite Level   |
                | 5. Edit Nitrate Level   |
                | 6. Edit Alkalinity      |
                | 7. Edit General Hardness|
                | 8. Done Editing         |
                +-------------------------+""");

        do {
            System.out.print("Choose Option: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> ws.setTemp(readDouble(input, "New Temperature: "));
                case 2 -> ws.setPHlvl(readDouble(input, "New pH Level: "));
                case 3 -> ws.setAmmoniaLvl(readDouble(input, "New Ammonia Level: "));
                case 4 -> ws.setNitriteLvl(readDouble(input, "New Nitrite Level: "));
                case 5 -> ws.setNitrateLvl(readDouble(input, "New Nitrate Level: "));
                case 6 -> ws.setAlkalinityLvl(readDouble(input, "New Alkalinity Level: "));
                case 7 -> ws.setGeneralHardness(readDouble(input, "New General Hardness: "));
                case 8 -> System.out.println("Finished editing.");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (choice != 8);
    }

    // ------------------------ REMOVE SAMPLE ------------------------
    public static void removeSample(String id, SampleLinkedList<WaterSample> normalList,
                                   TaskQueue<WaterSample> riskQueue) {
        boolean removed = false;

        if (removeFromList(normalList, id)) {
            removed = true;
        } else if (removeFromList(riskQueue.getList(), id)) {
            removed = true;
        }

        if (removed) {
            writeAllToFile(normalList, riskQueue);
            System.out.println("Sample Removed.");
            System.out.println();
        } else {
            System.out.println("Sample Not Found.");
        }
    }

    private static boolean removeFromList(SampleLinkedList<WaterSample> list, String id) {
        WaterSample prev = null;
        WaterSample current = list.getFirst();

        while (current != null) {
            if (current.getSampleID().equals(id)) {
                if (prev == null) {
                    list.removeFirst();
                } else {
                    list.removeAfter(prev);
                }
                return true;
            }
            prev = current;
            current = list.getNext();
        }
        return false;
    }

    // ------------------------ FILE WRITER ------------------------
    public static void writeAllToFile(SampleLinkedList<WaterSample> normalList,
                                     TaskQueue<WaterSample> riskQueue) {

        ArrayList<WaterSample> allSamples = new ArrayList<>();

        // Collect normal samples
        WaterSample ws = normalList.getFirst();
        while (ws != null) {
            allSamples.add(ws);
            ws = normalList.getNext();
        }

        // Collect risk samples
        SampleLinkedList<WaterSample> q = riskQueue.getList();
        ws = q.getFirst();
        while (ws != null) {
            allSamples.add(ws);
            ws = q.getNext();
        }

        // Sort Samples By sampleID (bubble sort)
        for (int i = 0; i < allSamples.size(); i++) {
            for (int j = 1; j < (allSamples.size() - i); j++) {
                if (allSamples.get(j - 1).getSampleID().compareTo(allSamples.get(j).getSampleID()) > 0) {
                    WaterSample temp = allSamples.get(j - 1);
                    allSamples.set(j - 1, allSamples.get(j));
                    allSamples.set(j, temp);
                }
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (WaterSample sample : allSamples) {
                pw.println(sample);
            }
        } catch (IOException e) {
            System.out.println("Error Writing File.");
        }
    }

    // ------------------------ INPUT VALIDATION METHODS ------------------------
    private static double readDouble(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextDouble()) {
                double value = input.nextDouble();
                input.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        }
    }

    private static LocalDate readDate(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine();
            try {
                return LocalDate.parse(text);
            } catch (Exception e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }
}
