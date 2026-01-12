import java.util.*;
public class sampleDisplay {
    static final int sample_per_row = 4;
    static final int row_per_page = 2;
    static final int samples_per_page = sample_per_row * row_per_page;
    static final int width = 30;

    public static void displayPaging(ArrayList<WaterSample> samples){
        Scanner in = new Scanner(System.in);
        int page = 0;
        int choice;

        do{
            clearScreen();
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
            if(page > 0) System.out.println("2 - Previous Page");
            if(end < samples.size()) System.out.println("1 - Next Page");
            System.out.println("3 - Sort by Date (Oldest to Newest)");
            System.out.println("4 - Sort by Risk Level (High to Low)");
            System.out.println("0 - Exit to Main Menu");
            System.out.print("Enter choice: ");
            choice = in.nextInt();

            if(choice == 1 && end < samples.size()) page++;
            else if(choice == 2 && page > 0) page--;
            else if (choice == 3){
                SortByDate(samples);
                page = 0;
            }
            else if (choice == 4){
                SortByRisk(samples);
                page = 0;
            }


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
        case "Low" -> 1;
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





    public static void clearScreen(){
        try{
            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("win")){
                new ProcessBuilder("cdm", "/c", "cls").inheritIO().start().waitFor();
            }
            else{
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        }catch(Exception e){
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
    }
