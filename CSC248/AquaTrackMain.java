
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AquaTrackMain {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        sampleDisplay sampleDisplay = new sampleDisplay();

        // ----------------------------------- APP LOOP -----------------------------------   
        while (true) {
            SampleLinkedList<WaterSample> normalList = new SampleLinkedList<>();
            TaskQueue<WaterSample> riskQueue = new TaskQueue<>();
            int opt = -1;

            // ----------------------------------- USER SIGN UP/LOGIN -----------------------------------   
            sampleDisplay.clearScreen();
            System.out.println("+-------------------------+");
            System.out.println("|   Welcome to AquaTrack! |");
            System.out.println("+-------------------------+");

            boolean loggedIn = false;

            while (!loggedIn) {
                System.out.print("Do you have an account? (y/n) | e-exit: ");
                String hasAccount = in.nextLine().toLowerCase();

                if (hasAccount.equals("y")) {
                    if (userAuth.login(in)) {
                        sampleDisplay.clearScreen();
                        loggedIn = true; // Exit loop on successful login
                    }
                } else if (hasAccount.equals("n")) {
                    userAuth.signUp(in);
                    System.out.println();
                } else if (hasAccount.equals("e")) {
                    System.out.println("Exiting AquaTrack. Goodbye!");
                    in.close();
                    return; // exit program
                } else {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            }

            // ----------------------------------- READ FILE & SEPARATE RECORDS -----------------------------------
            try (BufferedReader br = new BufferedReader(new FileReader("Samples.txt"))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");

                    String id = data[0];
                    double temp = Double.parseDouble(data[1]);
                    double pH = Double.parseDouble(data[2]);
                    double ammonia = Double.parseDouble(data[3]);
                    double nitrite = Double.parseDouble(data[4]);
                    double nitrate = Double.parseDouble(data[5]);
                    double alkalinity = Double.parseDouble(data[6]);
                    double gh = Double.parseDouble(data[7]);
                    LocalDate date = LocalDate.parse(data[9]); // risk is auto-calculated
                    boolean actionTaken = false;
                    if (data.length > 10) {
                        try {
                            actionTaken = Boolean.parseBoolean(data[10]);
                        } catch (Exception ignored) {
                        }
                    }

                    WaterSample ws;
                    if (data.length > 10) {
                        ws = new WaterSample(id, temp, pH, ammonia, nitrite, nitrate, alkalinity, gh, date, actionTaken);
                    } else {
                        ws = new WaterSample(id, temp, pH, ammonia, nitrite, nitrate, alkalinity, gh, date);
                    }

                    if (ws.getRiskLvl().equals("Normal")) {
                        normalList.addLast(ws);
                    } else {
                        riskQueue.enqueue(ws);
                    }
                }

            } catch (IOException e) {
                System.out.println("Error reading Samples.txt");
            }

            // ----------------------------------- START MENU -----------------------------------
            do {
                String sampleMenu = """
                    +------------------------+
                    | 1. Add sample          |
                    | 2. View sample(s)      |
                    | 3. Pending Task        |
                    | 0. Logout              |
                    +------------------------+
                    """;
                System.out.println(sampleMenu);
                System.out.print("Choose Option: ");

                try {
                    opt = in.nextInt();
                    if (opt == 1) {
                        sampleDisplay.clearScreen();

                        // ---------- CALL WATER SAMPLE MENU ----------
                        WaterSample.wSample(normalList, riskQueue);
                    } else if (opt == 2) {
                        sampleDisplay.clearScreen();

                        ArrayList<WaterSample> Samples = SampleDataLoader.loadFromFile("Samples.txt");

                        sampleDisplay.displayPaging(Samples);

                    } else if (opt == 3) {
                        sampleDisplay.clearScreen();
                        // load samples from file into lists before showing pending menu
                        normalList.clear();
                        // clear riskQueue by creating new one
                        riskQueue = new TaskQueue<>();

                        try (BufferedReader br = new BufferedReader(new FileReader("Samples.txt"))) {
                            String line;

                            while ((line = br.readLine()) != null) {
                                String[] data = line.split(",");

                                String id = data[0];
                                double temp = Double.parseDouble(data[1]);
                                double pH = Double.parseDouble(data[2]);
                                double ammonia = Double.parseDouble(data[3]);
                                double nitrite = Double.parseDouble(data[4]);
                                double nitrate = Double.parseDouble(data[5]);
                                double alkalinity = Double.parseDouble(data[6]);
                                double gh = Double.parseDouble(data[7]);
                                java.time.LocalDate date = java.time.LocalDate.parse(data[9]);
                                boolean actionTaken = false;
                                if (data.length > 10) {
                                    try {
                                        actionTaken = Boolean.parseBoolean(data[10]);
                                    } catch (Exception ignored) {
                                    }
                                }

                                WaterSample ws;
                                if (data.length > 10) {
                                    ws = new WaterSample(id, temp, pH, ammonia, nitrite, nitrate, alkalinity, gh, date, actionTaken);
                                } else {
                                    ws = new WaterSample(id, temp, pH, ammonia, nitrite, nitrate, alkalinity, gh, date);
                                }

                                if (ws.getRiskLvl().equals("Normal")) {
                                    normalList.addLast(ws);
                                } else {
                                    riskQueue.enqueue(ws);
                                }
                            }

                        } catch (IOException e) {
                            System.out.println("Error reading Samples.txt");
                        }

                        queuePending qp = new queuePending();
                        qp.queuePending(normalList, riskQueue);
                    } else if (opt == 0) {
                        sampleDisplay.clearScreen();
                        System.out.println("Logged Out Successfully");
                        in.nextLine();
                        break; // back to login menu
                    } else {
                        System.out.print("Invalid option. Please try again: ");
                    }

                } catch (Exception e) {
                    System.out.print("Invalid input. Please try again: ");
                    in.nextLine();
                }

            } while (opt != 0);
        }
    }
}
