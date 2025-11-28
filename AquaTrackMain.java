import java.util.Scanner;
public class AquaTrackMain{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        uiPrinter uiP = new uiPrinter();
        addSample add = new addSample();
        queuePending queue = new queuePending();
        viewSample view = new viewSample();
        System.out.print(uiP.menu()); //placeholder nanti tukar juga 
        int opt = input.nextInt();
        boolean optEntered = false;
        
        //pandai2 buat ui
        while (true) {
            optEntered = true;
            switch (opt){
            case 1:
                add.addSample();
                break;
            case 2:
                view.viewSample();
                break;
            
            case 3:
                queue.queuePending();
                break;
            
            default:
                System.out.println("Invalid input please try again....");
                break;
            }
            
        }
    }
}