import java.time.LocalDate;

public class WaterSample {
    private final String sampleID;
    private double temp, pHlvl, ammoniaLvl, nitriteLvl, nitrateLvl;
    private double alkalinityLvl, generalHardness;
    private LocalDate date;
    private String riskLvl;
    private boolean actionTaken = false;

    // ------------------------ CONSTRUCTOR ------------------------
    public WaterSample(String sampleID, double temp, double pHlvl, double ammoniaLvl,
                      double nitriteLvl, double nitrateLvl, double alkalinityLvl,
                      double generalHardness, LocalDate date) {
        this.sampleID = sampleID;
        this.temp = temp;
        this.pHlvl = pHlvl;
        this.ammoniaLvl = ammoniaLvl;
        this.nitriteLvl = nitriteLvl;
        this.nitrateLvl = nitrateLvl;
        this.alkalinityLvl = alkalinityLvl;
        this.generalHardness = generalHardness;
        this.date = date;
        this.actionTaken = false;
        determineRisk();
    }

    public WaterSample(String sampleID, double temp, double pHlvl, double ammoniaLvl,
                      double nitriteLvl, double nitrateLvl, double alkalinityLvl,
                      double generalHardness, LocalDate date, boolean actionTaken) {
        this.sampleID = sampleID;
        this.temp = temp;
        this.pHlvl = pHlvl;
        this.ammoniaLvl = ammoniaLvl;
        this.nitriteLvl = nitriteLvl;
        this.nitrateLvl = nitrateLvl;
        this.alkalinityLvl = alkalinityLvl;
        this.generalHardness = generalHardness;
        this.date = date;
        this.actionTaken = actionTaken;
        determineRisk();
    }

    // ------------------------ GETTER METHODS ------------------------
    public String getSampleID() { return sampleID; }
    public double getTemp() { return temp; }
    public double getPHlvl() { return pHlvl; }
    public double getAmmoniaLvl() { return ammoniaLvl; }
    public double getNitriteLvl() { return nitriteLvl; }
    public double getNitrateLvl() { return nitrateLvl; }
    public double getAlkalinityLvl() { return alkalinityLvl; }
    public double getGeneralHardness() { return generalHardness; }
    public String getRiskLvl() { return riskLvl; }
    public boolean isActionTaken() { return actionTaken; }
    public LocalDate getDate() { return date; }

    // ------------------------ SETTER METHODS ------------------------
    public void setTemp(double temp) { 
        this.temp = temp; 
        determineRisk();
    }
    public void setPHlvl(double pHlvl) { 
        this.pHlvl = pHlvl; 
        determineRisk();
    }
    public void setAmmoniaLvl(double ammoniaLvl) { 
        this.ammoniaLvl = ammoniaLvl; 
        determineRisk();
    }
    public void setNitriteLvl(double nitriteLvl) { 
        this.nitriteLvl = nitriteLvl; 
        determineRisk();
    }
    public void setNitrateLvl(double nitrateLvl) { 
        this.nitrateLvl = nitrateLvl; 
        determineRisk();
    }
    public void setAlkalinityLvl(double alkalinityLvl) { 
        this.alkalinityLvl = alkalinityLvl; 
        determineRisk();
    }
    public void setGeneralHardness(double generalHardness) { 
        this.generalHardness = generalHardness; 
        determineRisk();
    }
    public void setActionTaken(boolean actionTaken) { 
        this.actionTaken = actionTaken; 
    }

    // ------------------------ RISK CALCULATION ------------------------
    private void determineRisk() {
        int score = 0;

        if (temp < 18 || temp > 37) score++;
        if (pHlvl < 6.5 || pHlvl > 7.5) score++;
        if (ammoniaLvl > 0) score++;
        if (nitriteLvl > 0) score++;
        if (nitrateLvl > 50) score++;
        if (alkalinityLvl < 70 || alkalinityLvl > 150) score++;
        if (generalHardness < 70 || generalHardness > 200) score++;

        if (score <= 2) {
            riskLvl = "Normal";
        } else if (score <= 4) {
            riskLvl = "Moderate";
        } else {
            riskLvl = "High";
        }
    }

    // ------------------------ DISPLAY FORMATS ------------------------
    public String[] toCard() {
        return new String[] {
            "+---------------------------+",
            String.format("| %-25s |", "ID: " + sampleID),
            "+---------------------------+",
            String.format("| %-25s |", "Date: " + date),
            String.format("| %-25s |", String.format("Temp: %.1f °C", temp)),
            String.format("| %-25s |", String.format("pH: %.2f", pHlvl)),
            String.format("| %-25s |", String.format("Ammonia: %.2f", ammoniaLvl)),
            String.format("| %-25s |", String.format("Nitrite: %.2f", nitriteLvl)),
            String.format("| %-25s |", String.format("Nitrate: %.2f", nitrateLvl)),
            String.format("| %-25s |", String.format("Alkalinity: %.2f", alkalinityLvl)),
            String.format("| %-25s |", String.format("Hardness: %.2f", generalHardness)),
            "|---------------------------|",
            String.format("| %-25s |", "Risk Level: " + riskLvl),
            "+---------------------------+"
        };
    }

    public String[] SuggestCard() {
        String suggestion = generateAISuggestion(
            temp, pHlvl, ammoniaLvl, nitriteLvl, 
            nitrateLvl, alkalinityLvl, generalHardness
        );

        java.util.ArrayList<String> suggestionLines = new java.util.ArrayList<>();
        int maxLineLength = 27;
        
        while (!suggestion.isEmpty()) {
            if (suggestion.length() <= maxLineLength) {
                suggestionLines.add(suggestion);
                break;
            } else {
                int splitAt = suggestion.lastIndexOf(' ', maxLineLength);
                if (splitAt == -1) splitAt = maxLineLength;
                suggestionLines.add(suggestion.substring(0, splitAt));
                suggestion = suggestion.substring(splitAt).trim();
            }
        }

        java.util.List<String> card = new java.util.ArrayList<>();
        card.add("+-----------------------------+");
        card.add(String.format("| %-27s |", "ID: " + sampleID));
        card.add(String.format("| %-27s |", "Date: " + date));
        card.add(String.format("| %-27s |", "Risk: " + riskLvl));
        card.add("|-----------------------------|");
        card.add(String.format("| %-27s |", "AI Suggestion:"));
        for (String line : suggestionLines) {
            card.add(String.format("| %-27s |", line));
        }
        card.add("+-----------------------------+");

        return card.toArray(new String[0]);
    }

    // ------------------------ AI SUGGESTION GENERATOR ------------------------
    public static String generateAISuggestion(double temp, double pH, double ammonia,
                                             double nitrite, double nitrate,
                                             double alkalinity, double hardness) {
        StringBuilder suggestion = new StringBuilder();

        if (temp < 22) suggestion.append("Increase water temperature; ");
        else if (temp > 28) suggestion.append("Reduce water temperature; ");
        if (pH < 6.8) suggestion.append("Raise pH level; ");
        else if (pH > 7.5) suggestion.append("Lower pH level; ");
        if (ammonia > 0.02) suggestion.append("Partial water change to reduce ammonia; ");
        if (nitrite > 0.02) suggestion.append("Check filtration, reduce nitrite; ");
        if (nitrate > 40) suggestion.append("Perform water change to reduce nitrate; ");
        if (alkalinity < 70) suggestion.append("Add buffer to increase alkalinity; ");
        else if (alkalinity > 150) suggestion.append("Dilute water to lower alkalinity; ");
        if (hardness < 70) suggestion.append("Increase minerals to raise hardness; ");
        else if (hardness > 200) suggestion.append("Dilute water to reduce hardness; ");

        return suggestion.length() == 0 ? 
               "Water parameters normal; monitor regularly." : 
               suggestion.toString();
    }

    // ------------------------ STRING REPRESENTATION ------------------------
    @Override
    public String toString() {
        return sampleID + "," + temp + "," + pHlvl + "," + ammoniaLvl + "," + 
               nitriteLvl + "," + nitrateLvl + "," + alkalinityLvl + "," + 
               generalHardness + "," + riskLvl + "," + date + "," + actionTaken;
    }
}
