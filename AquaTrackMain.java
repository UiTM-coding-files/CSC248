import java.util.Scanner;
public class AquaTrackMain{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        uiPrinter uiP = new uiPrinter();
        addSample add = new addSample();
        System.out.print(uiP.menu());
        int opt = input.nextInt();
        boolean optEntered = false;

        while (true) {
            optEntered = true;
            if (opt == 1){
                add.addSample();
                break;
            }
            else if (opt == 2){
                System.out.println(uiP.viewSample());
                break;
            }
            else if (opt == 3){
                System.err.println(uiP.queuePending());
                break;
            }
            else{
                System.out.println("Invalid input please try again....");
                break;
            }
            
        }
    }
}