import java.util.Scanner;

public class queuePending {
    //Main menu untuk module Pending Task
    public void queuePending() {
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
        System.out.print(queueMenu);
        opt = in.nextInt();

        while (true) {
            switch (opt) {
                case 1:

                case 2:

                case 3:

                case 4:

                default:
                    break;
            }
        }
    }
    //Menu untuk pendingList
    public void pendingList(){

    }
    //Menu untuk update samples
    public void update(){

    }
    //Menu untuk AI suggestion
    public void suggest(){

    }
}
