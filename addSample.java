import java.util.Scanner;
import java.util.InputMismatchException;
public class addSample {
    public void addSample(){
        Scanner input = new Scanner (System.in);
        String sampleMenu = """
                +------------------------+
                | 1. Add new sample      |
                | 2. Search sample(s)    |
                | 3. Remove samples(s)   |
                +------------------------+
                """;
                System.out.println(sampleMenu);
                System.out.print("^");
                int opt = input.nextInt();

    }
}
