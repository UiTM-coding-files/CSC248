import java.util.Scanner;
public class AquaTrackMain{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        uiPrinter uiP = new uiPrinter();
        System.out.println(uiP.menu());
        int opt = input.nextInt();
        boolean optEntered = false;

        while (true) {
            optEntered = true;
            if (opt == 1){
                System.out.println(uiP.sample());
            }
            else if (opt == 2){
                System.out.println(uiP.viewSample());
            }
            else if (opt == 3){
                System.err.println(uiP.queuePending());
            }
            else{
                System.out.println("Invalid input please try again....");
            }
            
        }
    }
}