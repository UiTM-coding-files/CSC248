import java.util.*;
public class sampleDisplay {
    static final int sample_per_row = 4;
    static final int row_per_page = 2;
    static final int samples_per_page = sample_per_row * row_per_page;
    static final int width = 30;

    public static void displayPaging(ArrayList<WaterSample> samples){
        Scanner in = new Scanner(System.in);
        int page = 0;
        int choice = -1;

        do{
            //clearScreen();
            if(samples.isEmpty()){
                System.out.println("No samples to display.");
                break;
            }

            int start = page * samples_per_page;
            int end = Math.min(start + samples_per_page, samples.size());
            ArrayList<WaterSample> pagesamples = new ArrayList<>(samples.subList(start, end));
            printGrid(pagesamples);

            System.out.println("Page " + (page + 1) + " of " + ((samples.size() - 1) / samples_per_page + 1));

            System.out.println("\nOptions: ");
            if(end < samples.size()) System.out.println("1 - Next Page");
            if(page > 0) System.out.println("2 - Previous Page");
            System.out.println("3 - Sort by Date (Oldest to Newest)");
            System.out.println("4 - Sort by Risk Level (High to Low)");
            System.out.println("5 - View Statistics");
            System.out.println("0 - Exit to Main Menu");
            System.out.print("Enter choice: ");
            try{
            choice = in.nextInt();

                if(choice == 1 && end < samples.size()) {clearScreen(); page++;}
            else if(choice == 2 && page > 0) {clearScreen(); page--;}
            else if (choice == 3){
                clearScreen();
                SortByDate(samples);
                page = 0;
            }
            else if (choice == 4){
                clearScreen();
                SortByRisk(samples);
                page = 0;
            }
            else if (choice == 5){
                clearScreen();
                statistics(samples);
                System.out.println("\nPress Enter to return to paging view...");
                in.nextLine();
                in.nextLine(); 
            }
            else if (choice != 0){
                System.out.println("Invalid choice. Please try again.");
                System.out.println("Press Enter to continue...");
                in.nextLine();
                in.nextLine();}
        }catch(Exception e){
            System.out.println("Invalid input. Please enter a number corresponding to the options.");
            in.nextLine(); }


        }while(choice != 0);
    }

    public static void printGrid(ArrayList<WaterSample> samples){
        for(int i = 0; i<samples.size(); i += sample_per_row){
            int count = Math.min(sample_per_row, samples.size() - i);
            String[][] sample = new String[count][];
            for(int j = 0; j<count; j++){
                sample[j] = samples.get (i + j).toCard();
            }
            for(int line = 0; line<sample[0].length; line++){
                for(int s = 0; s<count; s++){
                    System.out.printf("%-" + width + "s", sample[s][line]);
                }
                System.out.println();
            } 
            System.out.println();
        }
    }

    public static void SortByDate(ArrayList<WaterSample> samples) {
    int n = samples.size();

    for (int j = 1; j < n; j++) {
        WaterSample key = samples.get(j);
        int i = j - 1;

        while (i >= 0 && samples.get(i).getDate().compareTo(key.getDate()) > 0) {
            samples.set(i + 1, samples.get(i));
            i--;
        }
        samples.set(i + 1, key);
    }
}

private static int riskValue(String risk) {
    return switch (risk) {
        case "High" -> 3;
        case "Moderate" -> 2;
        case "Normal" -> 1;
        default -> 0;
    };
}

public static void SortByRisk(ArrayList<WaterSample> data) {
    int n = data.size();

    for (int j = 1; j < n; j++) {
        WaterSample key = data.get(j);
        int i = j - 1;

        while (i >= 0 &&
               riskValue(data.get(i).getRiskLvl()) < riskValue(key.getRiskLvl())) {
            data.set(i + 1, data.get(i));
            i--;
        }
        data.set(i + 1, key);
    }
}

public static void statistics(ArrayList<WaterSample> samples) {
    int totalSamples = samples.size();
    int highRiskCount = 0;
    int moderateRiskCount = 0;
    int normalRiskCount = 0;


    for(int i = 0; i<samples.size(); i++){
        String risk = samples.get(i).getRiskLvl();
        if(risk.equalsIgnoreCase("High")){
            highRiskCount++;
        }
        else if(risk.equalsIgnoreCase("Moderate")){
            moderateRiskCount++;
        }
        else if(risk.equalsIgnoreCase("Normal")){
            normalRiskCount++;
        }
    }
    double highRiskPercentage = highRiskCount * 100.0 / totalSamples;
    double moderateRiskPercentage = moderateRiskCount * 100.0 / totalSamples;
    double normalRiskPercentage = normalRiskCount * 100.0 / totalSamples;

    System.out.println("Total Samples: " + totalSamples);
    System.out.println("High Risk Samples: " + highRiskCount);
    System.out.println("Moderate Risk Samples: " + moderateRiskCount);
    System.out.println("Normal Risk Samples: " + normalRiskCount);
    System.out.println("");
    System.out.println("Risk Level      Percentage");
    System.out.println("---------------------------");
    System.out.printf("%-15s %.2f%%%n", "High", highRiskPercentage);
    System.out.printf("%-15s %.2f%%%n", "Moderate", moderateRiskPercentage);
    System.out.printf("%-15s %.2f%%%n", "Normal", normalRiskPercentage);


}





    public static void clearScreen(){
                System.out.print("\033[H\033[2J");
                System.out.flush();
            
        }
    }
