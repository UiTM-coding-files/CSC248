
import java.io.*;
import java.time.LocalDate; // for sorting only
import java.util.ArrayList;
import java.util.Scanner;

public class WaterSample {

    private static final String FILE_NAME = "Samples.txt";

    private final String sampleID;
    private double temp, pHlvl, ammoniaLvl, nitriteLvl, nitrateLvl;
    private double alkalinityLvl, generalHardness;
    private LocalDate date;
    private String riskLvl;
    private boolean actionTaken = false;
    private static sampleDisplay sampleD = new sampleDisplay();

    // ------------------------ CONSTRUCTOR ------------------------
    public WaterSample(String sampleID, double temp, double pHlvl, double ammoniaLvl,
            double nitriteLvl, double nitrateLvl, double alkalinityLvl,
            double generalHardness, LocalDate date) {

        this.sampleID = sampleID;
        this.temp = temp;
        this.pHlvl = pHlvl;
        this.ammoniaLvl = ammoniaLvl;
        this.nitriteLvl = nitriteLvl;
        this.nitrateLvl = nitrateLvl;
        this.alkalinityLvl = alkalinityLvl;
        this.generalHardness = generalHardness;
        this.date = date;
        this.actionTaken = false;

        determineRisk();
    }

    public WaterSample(String sampleID, double temp, double pHlvl, double ammoniaLvl,
            double nitriteLvl, double nitrateLvl, double alkalinityLvl,
            double generalHardness, LocalDate date, boolean actionTaken) {

        this.sampleID = sampleID;
        this.temp = temp;
        this.pHlvl = pHlvl;
        this.ammoniaLvl = ammoniaLvl;
        this.nitriteLvl = nitriteLvl;
        this.nitrateLvl = nitrateLvl;
        this.alkalinityLvl = alkalinityLvl;
        this.generalHardness = generalHardness;
        this.date = date;
        this.actionTaken = actionTaken;

        determineRisk();
    }

    // ------------------------ GETTTER METHODS ------------------------
    public String getSampleID() {
        return sampleID;
    }

    public double getTemp() {
        return temp;
    }

    public double getPHlvl() {
        return pHlvl;
    }

    public double getAmmoniaLvl() {
        return ammoniaLvl;
    }

    public double getNitriteLvl() {
        return nitriteLvl;
    }

    public double getNitrateLvl() {
        return nitrateLvl;
    }

    public double getAlkalinityLvl() {
        return alkalinityLvl;
    }

    public double getGeneralHardness() {
        return generalHardness;
    }

    public String getRiskLvl() {
        return riskLvl;
    }

    public boolean isActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(boolean actionTaken) {
        this.actionTaken = actionTaken;
    }

    public LocalDate getDate() {
        return date;
    }

    // ------------------------ SETTTER METHODS ------------------------
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setPHlvl(double pHlvl) {
        this.pHlvl = pHlvl;
    }

    public void setAmmoniaLvl(double ammoniaLvl) {
        this.ammoniaLvl = ammoniaLvl;
    }

    public void setNitriteLvl(double nitriteLvl) {
        this.nitriteLvl = nitriteLvl;
    }

    public void setNitrateLvl(double nitrateLvl) {
        this.nitrateLvl = nitrateLvl;
    }

    public void setAlkalinityLvl(double alkalinityLvl) {
        this.alkalinityLvl = alkalinityLvl;
    }

    public void setGeneralHardness(double generalHardness) {
        this.generalHardness = generalHardness;
    }

    // ------------------------ SAMPLE METHODS ------------------------
    public static void wSample(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {
        Scanner input = new Scanner(System.in);

        int opt;

        do {
            String sampleMenu = """
                    +------------------------+
                    | 1. Add new sample      |
                    | 2. Search and Edit     |
                    | 3. Remove samples(s)   |
                    | 4. Back to Main Menu   |
                    +------------------------+
                    """;
            System.out.println(sampleMenu);
            System.out.print("Choose Option: ");
            opt = input.nextInt();
            input.nextLine();

            switch (opt) {
                case 1:
                    sampleD.clearScreen();
                    addSample(input, normalList, riskQueue);
                    break;

                case 2:
                    sampleD.clearScreen();
                    System.out.print("Enter sample ID to Search: ");
                    String key = input.nextLine();
                    SearchAndEdit(key, normalList, riskQueue, input);
                    break;

                case 3:
                    sampleD.clearScreen();
                    System.out.print("Enter sample ID to Remove: ");
                    String id = input.nextLine();
                    removeSample(id, normalList, riskQueue);
                    break;

                case 4:
                    sampleD.clearScreen();
                    break;

                default:
                    System.out.println("Invalid Option. Try Again.");
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

        WaterSample ws = new WaterSample(
                id, temp, pH, ammonia, nitrite, nitrate, alk, gh, date);

        if (ws.getRiskLvl().equalsIgnoreCase("Normal")) {
            normalList.addLast(ws);
        } else {
            riskQueue.enqueue(ws);
        }

        writeAllToFile(normalList, riskQueue);

        System.out.println("[ ID: " + id + ", Risk Level: " + ws.getRiskLvl() + " ]");
        System.out.println("Sample added successfully.\n");
    }

    // ------------------------ DETERMINE RISK LEVEL ------------------------
    private void determineRisk() {
        int score = 0;

        if (temp < 18 || temp > 37) {
            score++;
        }
        if (pHlvl < 6.5 || pHlvl > 7.5) {
            score++;
        }
        if (ammoniaLvl > 0) {
            score++;
        }
        if (nitriteLvl > 0) {
            score++;
        }
        if (nitrateLvl > 50) {
            score++;
        }
        if (alkalinityLvl < 70 || alkalinityLvl > 150) {
            score++;
        }
        if (generalHardness < 70 || generalHardness > 200) {
            score++;
        }

        if (score <= 2) {
            riskLvl = "Normal";
        } else if (score <= 4) {
            riskLvl = "Moderate";
        } else {
            riskLvl = "High";
        }
    }

    // ------------------------ SEARCH AND EDIT ------------------------
    public static void SearchAndEdit(String key, SampleLinkedList<WaterSample> normalList,
            TaskQueue<WaterSample> riskQueue, Scanner input) {

        // Search in Normal List
        WaterSample ws = normalList.getFirst();
        while (ws != null) {
            if (ws.getSampleID().equals(key)) {
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
                    System.out.println(" ");
                    System.out.println("Sample Updated.");
                    for (String line : ws.toCard()) {
                    System.out.println(line);
                    }
                    System.out.println(" ");
                } else {
                    System.out.println("Edit cancelled.");
                }
                return; // go back to menu
            }
            ws = normalList.getNext();
        }

        // Search in Queue List
        SampleLinkedList<WaterSample> q = riskQueue.getList();
        ws = q.getFirst();
        while (ws != null) {
            if (ws.sampleID.equals(key)) {
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
                    System.out.println(" ");
                    System.out.println("Sample Updated.");
                    for (String line : ws.toCard()) {
                    System.out.println(line);
                    }
                    System.out.println(" ");
                } else {
                    System.out.println("Edit cancelled.");
                }
                return; // go back to menu
            }
            ws = q.getNext();
        }

        // not found
        System.out.println("Sample Not Found.\n");
    }

    // ------------------------ EDIT SAMPLE ------------------------
    private static void editSample(WaterSample ws, Scanner input) {
        int choice;

        System.out.println("+-------------------------+");
        System.out.println("| 1. Edit Temperature     |");
        System.out.println("| 2. Edit pH Level        |");
        System.out.println("| 3. Edit Ammonia Level   |");
        System.out.println("| 4. Edit Nitrite Level   |");
        System.out.println("| 5. Edit Nitrate Level   |");
        System.out.println("| 6. Edit Alkalinity      |");
        System.out.println("| 7. Edit General Hardness|");
        System.out.println("| 8. Done Editing         |");
        System.out.println("+-------------------------+");

        do {
            System.out.print("Choose Option: ");
            choice = input.nextInt();
            input.nextLine(); // clear buffer

            switch (choice) {
                case 1 ->
                    ws.setTemp(readDouble(input, "New Temperature: "));
                case 2 ->
                    ws.setPHlvl(readDouble(input, "New pH Level: "));
                case 3 ->
                    ws.setAmmoniaLvl(readDouble(input, "New Ammonia Level: "));
                case 4 ->
                    ws.setNitriteLvl(readDouble(input, "New Nitrite Level: "));
                case 5 ->
                    ws.setNitrateLvl(readDouble(input, "New Nitrate Level: "));
                case 6 ->
                    ws.setAlkalinityLvl(readDouble(input, "New Alkalinity Level: "));
                case 7 ->
                    ws.setGeneralHardness(readDouble(input, "New General Hardness: "));
                case 8 ->
                    System.out.println("Finished editing.");
                default ->
                    System.out.println("Invalid option. Try again.");
            }

            ws.determineRisk();

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


    // ------------------------ TO CARD FORMAT ------------------------
    public String[] toCard() {
        return new String[]{
            "+---------------------------+",
            String.format("| %-25s |", "ID: " + sampleID),
            "+---------------------------+",
            String.format("| %-25s |", "Date: " + date),
            String.format("| %-25s |", String.format("Temp: %.1f °C", temp)),
            String.format("| %-25s |", String.format("pH: %.2f", pHlvl)),
            String.format("| %-25s |", String.format("Ammonia: %.2f", ammoniaLvl)),
            String.format("| %-25s |", String.format("Nitrite: %.2f", nitriteLvl)),
            String.format("| %-25s |", String.format("Nitrate: %.2f", nitrateLvl)),
            String.format("| %-25s |", String.format("Alkalinity: %.2f", alkalinityLvl)),
            String.format("| %-25s |", String.format("Hardness: %.2f", generalHardness)),
            "|---------------------------|",
            String.format("| %-25s |", "Risk Level: " + riskLvl),
            "+---------------------------+"
        };
    }

    // ------------------------ TO SUGGESTION CARD FORMAT ------------------------
    public String[] SuggestCard() {

        String suggestion;

        if (riskLvl.equalsIgnoreCase("High")) {
            suggestion = "Immediate action required";
        } else if (riskLvl.equalsIgnoreCase("Moderate")) {
            suggestion = "Re-test within 24 hours";
        } else {
            suggestion = "Monitor (no urgent action)";
        }

        return new String[]{
            "+-----------------------------+",
            String.format("| %-27s |", "ID: " + sampleID),
            String.format("| %-27s |", "Date: " + date),
            String.format("| %-27s |", "Risk: " + riskLvl),
            "|-----------------------------|",
            String.format("| %-27s |", "Suggestion:"),
            String.format("| %-27s |", suggestion),
            "+-----------------------------+"
        };
    }

    // ------------------------ FILE WRITER ------------------------
    public static void writeAllToFile(SampleLinkedList<WaterSample> normalList,
            TaskQueue<WaterSample> riskQueue) {

        ArrayList<WaterSample> allSamples = new ArrayList<>();

        // collect normal samples
        WaterSample ws = normalList.getFirst();
        while (ws != null) {
            allSamples.add(ws);
            ws = normalList.getNext();
        }

        // collect risk samples
        SampleLinkedList<WaterSample> q = riskQueue.getList();
        ws = q.getFirst();
        while (ws != null) {
            allSamples.add(ws);
            ws = q.getNext();
        }

        // Sort Samples By sampleID
        int n = allSamples.size();
        WaterSample temp;

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (allSamples.get(j - 1).getSampleID().compareTo(allSamples.get(j).getSampleID()) > 0) {
                    // swap elements
                    temp = allSamples.get(j - 1);
                    allSamples.set(j - 1, allSamples.get(j));
                    allSamples.set(j, temp);
                }
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < allSamples.size(); i++) {
                pw.println(allSamples.get(i));
            }
        } catch (IOException e) {
            System.out.println("Error Writing File.");
        }
    }

    // --------------- Input Handler for Add Sample ---------------
    private static double readDouble(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextDouble()) {
                double value = input.nextDouble();
                input.nextLine(); // clear buffer
                return value;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine(); // discard wrong input
            }
        }
    }

    private static LocalDate readDate(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine();
            try {
                return LocalDate.parse(text); // YYYY-MM-DD
            } catch (Exception e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

}
