import java.util.ArrayList;
import java.util.Scanner;

public class AquaTrackMain {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        sampleDisplay sampleDisplay = new sampleDisplay();

        while (true) {

            SampleLinkedList<WaterSample> normalList = new SampleLinkedList<>();
            TaskQueue<WaterSample> riskQueue = new TaskQueue<>();
            int opt = -1;

            // ================= LOGIN =================
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
                        loggedIn = true;
                    }
                } else if (hasAccount.equals("n")) {
                    userAuth.signUp(in);
                } else if (hasAccount.equals("e")) {
                    in.close();
                    return;
                }
            }

            // ================= LOAD FILE =================
            SampleDataLoader.loadSamples(normalList, riskQueue);

            // ================= MENU =================
            do {
                System.out.println("""
                    +------------------------+
                    | 1. Add sample          |
                    | 2. View sample(s)      |
                    | 3. Pending Task        |
                    | 0. Logout              |
                    +------------------------+
                    """);
                System.out.print("Choose Option: ");

                try {
                    opt = in.nextInt();
                    in.nextLine();

                    if (opt == 1) {
                        sampleDisplay.clearScreen();
                        WaterSample.wSample(normalList, riskQueue);

                    } else if (opt == 2) {
                        sampleDisplay.clearScreen();
                        ArrayList<WaterSample> samples =
                                SampleDataLoader.loadFromFile("Samples.txt");
                        sampleDisplay.displayPaging(samples);

                    } else if (opt == 3) {
                        sampleDisplay.clearScreen();
                        normalList.clear();
                        riskQueue = new TaskQueue<>();
                        SampleDataLoader.loadSamples(normalList, riskQueue);

                        queuePending qp = new queuePending();
                        qp.queuePending(normalList, riskQueue);

                    } else if (opt == 0) {
                        sampleDisplay.clearScreen();
                        System.out.println("Logged Out Successfully");
                        break;
                    }

                } catch (Exception e) {
                    in.nextLine();
                }

            } while (opt != 0);
        }
    }

}