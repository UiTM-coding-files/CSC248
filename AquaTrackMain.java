import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class AquaTrackMain{

    public static void main(String[] args){

        int opt = -1;
        Scanner in = new Scanner(System.in);
        SampleLinkedList<WaterSample> normalList = new SampleLinkedList<>();
        TaskQueue<WaterSample> riskQueue = new TaskQueue<>();


    do{
        System.out.print("Enter 1(add),2(view),3,0(exit): ");
        opt = in.nextInt();
            if(opt == 1){
        // ---------- READ FILE & SEPARATE ----------
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
                String date = data[9]; // risk is auto-calculated

                WaterSample ws = new WaterSample(
                        id, temp, pH, ammonia, nitrite,
                        nitrate, alkalinity, gh, date
                );

                if (ws.getRiskLvl().equals("Normal")) {
                    normalList.addLast(ws);
                } else {
                    riskQueue.enqueue(ws);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading Samples.txt");
        }

        // ---------- CALL WATER SAMPLE MENU ----------
        WaterSample.wSample(normalList, riskQueue);
    }

    else if(opt == 2){

            ArrayList<WaterSample> Samples = SampleDataLoader.loadFromFile("Samples.txt");

            sampleDisplay.displayPaging(Samples);
        
    }
    else if(opt == 3){

    }




    }while(opt != 0);
        }
    }
