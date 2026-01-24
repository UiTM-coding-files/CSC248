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
                    | 2. Search sample(s)    |
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
                    addSample(input, normalList, riskQueue);
                    break;

                case 2:
                    System.out.print("Enter sample ID to Search: ");
                    String key = input.nextLine();
                    SearchAndEdit(key, normalList, riskQueue, input);
                    break;

                case 3:
                    System.out.print("Enter sample ID to Remove: ");
                    String id = input.nextLine();
                    removeSample(id, normalList, riskQueue);
                    break;

                case 4:
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
    public static void addSample(Scanner input, SampleLinkedList<WaterSample> normalList,
            TaskQueue<WaterSample> riskQueue) {

        String id = generateID();
        System.out.print("Temperature: ");
        double temp = input.nextDouble();
        System.out.print("pH Level: ");
        double pH = input.nextDouble();
        System.out.print("Ammonia Level: ");
        double ammonia = input.nextDouble();
        System.out.print("Nitrite Level: ");
        double nitrite = input.nextDouble();
        System.out.print("Nitrate Level: ");
        double nitrate = input.nextDouble();
        System.out.print("Alkalinity Level: ");
        double alk = input.nextDouble();
        System.out.print("General Hardness: ");
        double gh = input.nextDouble();
        System.out.print("Date [YYYY-MM-DD]: ");
        input.nextLine();
        String ip = input.nextLine();
        LocalDate date = LocalDate.parse(ip);

        WaterSample ws = new WaterSample(id, temp, pH, ammonia, nitrite, nitrate, alk, gh, date);

        if (ws.getRiskLvl().equals("Normal")) {
            normalList.addLast(ws);
        } else {
            riskQueue.enqueue(ws);
        }

        writeAllToFile(normalList, riskQueue);
        System.out.println("[ ID: " + id + ", Risk Level: " + ws.getRiskLvl() + "]");
        System.out.println("Sample added.");
        System.out.println();
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
                for (String line : ws.toCard()) {
                System.out.println(line);
}

                System.out.print("Do you want to edit this sample? [Y/N]: ");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("Y")) {
                    editSample(ws, input);
                    writeAllToFile(normalList, riskQueue);
                    System.out.println("Sample Updated.\n");
                } else {
                    System.out.println("Edit cancelled.\n");
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
                for (String line : ws.toCard()) {
                System.out.println(line);
            }

                System.out.print("Do you want to edit this sample? [Y/N]: ");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("Y")) {
                    editSample(ws, input);
                    writeAllToFile(normalList, riskQueue);
                    System.out.println("Sample Updated.\n");
                } else {
                    System.out.println("Edit cancelled.\n");
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
        System.out.print("New Temperature: ");
        ws.setTemp(input.nextDouble());
        System.out.print("New pH Level: ");
        ws.setPHlvl(input.nextDouble());
        System.out.print("New Ammonia Level: ");
        ws.setAmmoniaLvl(input.nextDouble());
        System.out.print("New Nitrite Level: ");
        ws.setNitriteLvl(input.nextDouble());
        System.out.print("New Nitrate Level: ");
        ws.setNitrateLvl(input.nextDouble());
        System.out.print("New Alkalinity Level: ");
        ws.setAlkalinityLvl(input.nextDouble());
        System.out.print("New General Hardness: ");
        ws.setGeneralHardness(input.nextDouble());
        input.nextLine();

        ws.determineRisk();
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

    // ------------------------ DISPLAY SAMPLE ------------------------
    public void displayFullSample() {
        System.out.println("ID: " + sampleID);
        System.out.println("Temperature: " + temp);
        System.out.println("pH Level: " + pHlvl);
        System.out.println("Ammonia Level: " + ammoniaLvl);
        System.out.println("Nitrite Level: " + nitriteLvl);
        System.out.println("Nitrate Level: " + nitrateLvl);
        System.out.println("Alkalinity Level: " + alkalinityLvl);
        System.out.println("General Hardness: " + generalHardness);
        System.out.println("Risk Level: " + riskLvl);
        System.out.println("Date: " + date);
    }

    // ------------------------ TO CARD FORMAT ------------------------
    public String[] toCard() {
        return new String[] {
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

        return new String[] {
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

    @Override
    public String toString() {
        return sampleID + ","
                + temp + ","
                + pHlvl + ","
                + ammoniaLvl + ","
                + nitriteLvl + ","
                + nitrateLvl + ","
                + alkalinityLvl + ","
                + generalHardness + ","
                + riskLvl + ","
                + date + "," + actionTaken;
    }
}