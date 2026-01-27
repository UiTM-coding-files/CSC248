import java.util.*;

public class sampleDisplay {
    private static final int sample_per_row = 5;
    private static final int row_per_page = 2;
    private static final int samples_per_page = sample_per_row * row_per_page;
    private static final int width = 30;

    public static void displayPaging(ArrayList<WaterSample> samples) {
        Scanner in = new Scanner(System.in);
        int page = 0;
        int choice = -1;

        do {
            // clearScreen();
            if (samples.isEmpty()) {
                System.out.println("No samples to display.");
                break;
            }

            int start = page * samples_per_page;
            int end = Math.min(start + samples_per_page, samples.size());
            ArrayList<WaterSample> pagesamples = new ArrayList<>(samples.subList(start, end));
            printGrid(pagesamples);

            String pageInfo = "Page " + (page + 1) + " of " + ((samples.size() - 1) / samples_per_page + 1);
            System.out.printf("%-60s%80s%n", "Options:", pageInfo);

            System.out.println("\nOptions: ");

            if (end < samples.size())
                System.out.println("1 - Next Page");
            if (page > 0)
                System.out.println("2 - Previous Page");
            System.out.println("3 - More Options ");
            System.out.println("0 - Exit to Main Menu");
            System.out.print("Enter choice: ");
            try {
                choice = in.nextInt();

                if (choice == 1 && end < samples.size()) {
                    clearScreen();
                    page++;
                } else if (choice == 2 && page > 0) {
                    clearScreen();
                    page--;
                } else if (choice == 3) {
                    clearScreen();
                    do {
                        start = page * samples_per_page;
                        end = Math.min(start + samples_per_page, samples.size());
                        pagesamples = new ArrayList<>(samples.subList(start, end));
                        printGrid(pagesamples);

                        System.out.printf("%-60s%80s%n", "More Options:", pageInfo);
                        System.out.println("4 - Sort by Date (Newest to Oldest)");
                        System.out.println("5 - Sort by Risk Level (High to Normal)");
                        System.out.println("6 - View Statistics");
                        System.out.println("7 - Back to Previous Menu");
                        System.out.print("Enter choice: ");
                        try {
                            choice = in.nextInt();
                            if (choice == 4) {
                                clearScreen();
                                SortByDate(samples);
                                page = 0;
                            } else if (choice == 5) {
                                clearScreen();
                                SortByRisk(samples);
                                page = 0;
                            } else if (choice == 6) {
                                clearScreen();
                                statistics(samples);
                                System.out.println("Press Enter to return...");
                                in.nextLine();
                                in.nextLine();
                            }

                            else if (choice != 7) {
                                System.out.println("Invalid choice. Please try again.");
                                System.out.println("Press Enter to continue...");
                                in.nextLine();
                                in.nextLine();
                                clearScreen();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number corresponding to the options.");
                            in.nextLine();
                        }
                    } while (choice != 7);
                    clearScreen();
                    page = 0;
                } else if (choice != 0) {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press Enter to continue...");
                    in.nextLine();
                    in.nextLine();
                    clearScreen();
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number corresponding to the options.");
                in.nextLine();
            }

        } while (choice != 0);
        clearScreen();
    }

    public static void display(ArrayList<WaterSample> samples) {
        Scanner in = new Scanner(System.in);
        int page = 0;
        int choice = -1;

        do {
            // clearScreen();
            System.out.println("Pending Maintenance List (Moderate/High risk, action not taken):\n");

            if (samples.isEmpty()) {
                System.out.println("No samples to display.");
                break;
            }

            int start = page * samples_per_page;
            int end = Math.min(start + samples_per_page, samples.size());
            ArrayList<WaterSample> pagesamples = new ArrayList<>(samples.subList(start, end));
            printGrid(pagesamples);

            String pageInfo = "Page " + (page + 1) + " of " + ((samples.size() - 1) / samples_per_page + 1);
            System.out.printf("%-60s%80s%n", "Options:", pageInfo);
            if (end < samples.size())
                System.out.println("1 - Next Page");
            if (page > 0)
                System.out.println("2 - Previous Page");
            System.out.println("0 - Exit to Main Menu");
            System.out.print("Enter choice: ");
            try {
                choice = in.nextInt();

                if (choice == 1 && end < samples.size()) {
                    clearScreen();
                    page++;
                } else if (choice == 2 && page > 0) {
                    clearScreen();
                    page--;
                } else if (choice != 0) {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press Enter to continue...");
                    in.nextLine();
                    in.nextLine();
                }
            } catch (Exception e) {
                clearScreen();
                System.out.println("Invalid input. Please enter a number corresponding to the options.");
                in.nextLine();
            }

        } while (choice != 0);
        clearScreen();
    }

    public static void suggestDisplay(ArrayList<WaterSample> result) {
        Scanner in = new Scanner(System.in);
        int page = 0;
        int choice = -1;

        do {
            clearScreen();

            if (result.isEmpty()) {
                System.out.println("No samples to display.");
                break;
            }

            int start = page * samples_per_page;
            int end = Math.min(start + samples_per_page, result.size());
            ArrayList<WaterSample> pagesamples = new ArrayList<>(result.subList(start, end));
            printGridsuggest(pagesamples);

            String pageInfo = "Page " + (page + 1) + " of " + ((result.size() - 1) / samples_per_page + 1);
            System.out.println(pageInfo);

            System.out.println("\nOptions: ");
            if (end < result.size())
                System.out.println("1 - Next Page");
            if (page > 0)
                System.out.println("2 - Previous Page");
            System.out.println("3 - More Options ");
            System.out.println("0 - Exit to Main Menu");
            System.out.print("Enter choice: ");
            try {
                choice = in.nextInt();
                in.nextLine();

                if (choice == 1 && end < result.size()) {
                    page++;
                } else if (choice == 2 && page > 0) {
                    page--;
                } else if (choice == 3) {
                    clearScreen();
                    do {
                        start = page * samples_per_page;
                        end = Math.min(start + samples_per_page, result.size());
                        pagesamples = new ArrayList<>(result.subList(start, end));
                        printGridsuggest(pagesamples);

                        System.out
                                .println("Page " + (page + 1) + " of " + ((result.size() - 1) / samples_per_page + 1));
                        System.out.println("\nMore Options:");
                        System.out.println("4 - Sort by Date (Newest to Oldest)");
                        System.out.println("5 - Sort by Risk Level (High to Normal)");
                        System.out.println("6 - View Statistics");
                        System.out.println("7 - Back to Previous Menu");
                        System.out.print("Enter choice: ");
                        try {
                            choice = in.nextInt();
                            in.nextLine();
                            if (choice == 4) {
                                SortByDate(result);
                                page = 0;
                                clearScreen();
                            } else if (choice == 5) {
                                SortByRisk(result);
                                page = 0;
                                clearScreen();
                            } else if (choice == 6) {
                                statistics(result);
                                System.out.println("Press Enter to return...");
                                in.nextLine();
                                clearScreen();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number corresponding to the options.");
                            in.nextLine();
                        }
                    } while (choice != 7);
                    page = 0;
                } else if (choice != 0) {
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("Press Enter to continue...");
                    in.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number corresponding to the options.");
                in.nextLine();
            }

        } while (choice != 0);
        clearScreen();
    }

    public static void printGrid(ArrayList<WaterSample> samples) {
        for (int i = 0; i < samples.size(); i += sample_per_row) {
            int count = Math.min(sample_per_row, samples.size() - i);
            String[][] sample = new String[count][];
            for (int j = 0; j < count; j++) {
                sample[j] = samples.get(i + j).toCard();
            }
            for (int line = 0; line < sample[0].length; line++) {
                for (int s = 0; s < count; s++) {
                    System.out.printf("%-" + width + "s", sample[s][line]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void printGridsuggest(ArrayList<WaterSample> samples) {
        Scanner in = new Scanner(System.in);
        int samplesPerPage = 3;
        int page = 0;
        int choice = -1;

        do {
            clearScreen();

            if (samples.isEmpty()) {
                System.out.println("No suggestions to display.");
                break;
            }

            int start = page * samplesPerPage;
            int end = Math.min(start + samplesPerPage, samples.size());

            System.out.println("=====================================================");
            System.out.println("                    AI SUGGESTIONS");
            System.out.println("=====================================================\n");

            for (int i = start; i < end; i++) {
                WaterSample sample = samples.get(i);

                // Sample info box
                System.out.println("+---------------------------------------------------+");
                System.out.printf("| Sample ID: %-38s |\n", sample.getSampleID());
                System.out.printf("| Date: %-43s |\n", sample.getDate());
                System.out.printf("| Risk Level: %-37s |\n", sample.getRiskLvl());
                System.out.println("+---------------------------------------------------+");

                // Get suggestions
                String suggestion = WaterSample.generateAISuggestion(
                        sample.getTemp(), sample.getPHlvl(), sample.getAmmoniaLvl(),
                        sample.getNitriteLvl(), sample.getNitrateLvl(),
                        sample.getAlkalinityLvl(), sample.getGeneralHardness());

                System.out.println("| AI Suggestions:                                   |");
                System.out.println("+---------------------------------------------------+");

                // Display suggestions with proper formatting
                String[] suggestions = suggestion.split("; ");
                for (String sugg : suggestions) {
                    String trimmed = sugg.trim();
                    if (!trimmed.isEmpty()) {
                        // Handle long lines with proper indentation
                        if (trimmed.length() > 50) {
                            int splitAt = trimmed.lastIndexOf(' ', 50);
                            if (splitAt == -1)
                                splitAt = 50;
                            System.out.printf("| - %-47s |\n", trimmed.substring(0, splitAt));
                            trimmed = trimmed.substring(splitAt).trim();
                            // Continuation lines
                            while (trimmed.length() > 50) {
                                splitAt = trimmed.lastIndexOf(' ', 50);
                                if (splitAt == -1)
                                    splitAt = 50;
                                System.out.printf("|   %-47s |\n", trimmed.substring(0, splitAt));
                                trimmed = trimmed.substring(splitAt).trim();
                            }
                            if (!trimmed.isEmpty()) {
                                System.out.printf("|   %-47s |\n", trimmed);
                            }
                        } else {
                            System.out.printf("| - %-47s |\n", trimmed);
                        }
                    }
                }

                System.out.println("+---------------------------------------------------+\n");
            }

            // Page info
            String pageInfo = "Page " + (page + 1) + " of " + ((samples.size() - 1) / samplesPerPage + 1);
            System.out.println(" ".repeat(25) + pageInfo);

            // Navigation menu
            System.out.println("\n+--------------------------+");
            System.out.println("| 1. Next Page             |");
            if (page > 0)
                System.out.println("| 2. Previous Page        |");
            System.out.println("| 3. More Options          |");
            System.out.println("| 0. Exit to Menu          |");
            System.out.println("+--------------------------+");
            System.out.print("Choice: ");

            try {
                choice = in.nextInt();
                in.nextLine();

                if (choice == 1 && end < samples.size()) {
                    page++;
                } else if (choice == 2 && page > 0) {
                    page--;
                } else if (choice == 3) {
                    // More options submenu
                    clearScreen();
                    System.out.println("+-----------------------------------+");
                    System.out.println("|         MORE OPTIONS              |");
                    System.out.println("+-----------------------------------+");
                    System.out.println("| 4. Sort by Date (Newest to Oldest)|");
                    System.out.println("| 5. Sort by Risk (High to Normal)  |");
                    System.out.println("| 6. Back to Suggestions            |");
                    System.out.println("+-----------------------------------+");
                    System.out.print("Choice: ");

                    int moreChoice = in.nextInt();
                    in.nextLine();

                    if (moreChoice == 4) {
                        SortByDate(samples);
                        page = 0;
                    } else if (moreChoice == 5) {
                        SortByRisk(samples);
                        page = 0;
                    }
                    // If choice 6, it will just loop back
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Press Enter to continue...");
                in.nextLine();
            }

        } while (choice != 0);
        clearScreen();
    }

    public static void SortByDate(ArrayList<WaterSample> samples) {
        int n = samples.size();

        for (int j = 1; j < n; j++) {
            WaterSample key = samples.get(j);
            int i = j - 1;

            while (i >= 0 && samples.get(i).getDate().compareTo(key.getDate()) < 0) {
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

        for (int i = 0; i < samples.size(); i++) {
            String risk = samples.get(i).getRiskLvl();
            if (risk.equalsIgnoreCase("High")) {
                highRiskCount++;
            } else if (risk.equalsIgnoreCase("Moderate")) {
                moderateRiskCount++;
            } else if (risk.equalsIgnoreCase("Normal")) {
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

    static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                new ProcessBuilder("clear")
                        .inheritIO()
                        .start()
                        .waitFor();
            }
        } catch (Exception e) {
            // Fallback (ANSI)
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

}
