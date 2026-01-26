import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class SampleDataLoader {

    private static final String FILE_NAME = "Samples.txt";

    static ArrayList<WaterSample> loadFromFile(String Samples) {
        ArrayList<WaterSample> sampleList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
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
                LocalDate date = LocalDate.parse(data[9]);
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
                sampleList.add(ws);
            }
        } catch (IOException e) {
            System.out.println("Error reading Samples.txt");
        }
        return sampleList;
    }

    static void loadSamples(SampleLinkedList<WaterSample> normalList, TaskQueue<WaterSample> riskQueue) {

        try (BufferedReader br = new BufferedReader(new FileReader("Samples.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                // PREVENT ArrayIndexOutOfBounds
                if (data.length < 10) {
                    continue;
                }

                String id = data[0];
                double temp = Double.parseDouble(data[1]);
                double pH = Double.parseDouble(data[2]);
                double ammonia = Double.parseDouble(data[3]);
                double nitrite = Double.parseDouble(data[4]);
                double nitrate = Double.parseDouble(data[5]);
                double alkalinity = Double.parseDouble(data[6]);
                double gh = Double.parseDouble(data[7]);
                LocalDate date = LocalDate.parse(data[9]);

                boolean actionTaken = false;
                if (data.length > 10) {
                    actionTaken = Boolean.parseBoolean(data[10]);
                }

                WaterSample ws = (data.length > 10)
                        ? new WaterSample(id, temp, pH, ammonia, nitrite,
                                nitrate, alkalinity, gh, date, actionTaken)
                        : new WaterSample(id, temp, pH, ammonia, nitrite,
                                nitrate, alkalinity, gh, date);

                if (ws.getRiskLvl().equals("Normal")) {
                    normalList.addLast(ws);
                } else {
                    riskQueue.enqueue(ws);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading Samples.txt");
        }

    }
}
