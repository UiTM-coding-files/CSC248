public class uiPrinter {
    public String menu(){
        return """
                +--------------------------------------------+
                |  AquaTrack Water Quality Monitoring System |
                +--------------------------------------------+

                +---------------------------+
                |1. Add new sample          |
                |2. View sample/s           |
                |3. View pending task/s     |
                +---------------------------+

                ^
                """;
    }
    public String sample(){
        return """
                +-----------------------+
                |   Adding new sample   |
                +-----------------------+ 
                """;
    }
    public String viewSample(){
        return """
                +-----------------------+
                |   List/s of sample   |
                +-----------------------+ 
                """;
    }
    public String queuePending(){
        return """
                +-----------------------+
                |   Pending Task in queue   |
                +-----------------------+ 
                """;
    }
}
