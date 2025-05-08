package testBase;
import java.io.*;
import java.text.*;
import java.util.*;

public class DeleteOldLogs {
    public void DeleteOldLogs() {
    	
        String logFilePath = "C:\\xampp\\htdocs\\lexerpos\\oz_aggregator\\logs\\logs.log";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Only consider the date part (no time)
        String currentDateString = dateFormat.format(new Date()); // Get today's date in yyyy-MM-dd format


        File logFile = new File(logFilePath);

        // Check if the log file exists
        if (!logFile.exists()) {
            System.out.println("Log file does not exist. Creating a new file.");
            try {
                // If the file doesn't exist, create a new empty file
                logFile.createNewFile();
                System.out.println("New log file created: " + logFilePath);
            } catch (IOException e) {
                System.err.println("Error creating the log file: " + e.getMessage());
                return; // Exit the program if the file can't be created
            }
        }
        
        try {
            // Read the file into a list
            File file = new File(logFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                // Assuming the log starts with a timestamp in the format "yyyy-MM-dd HH:mm:ss"
                String timestampString = line.split(" ")[0]; // Extract the date part (first part of the line)
                
                if (timestampString.equals(currentDateString)) { // Check if the date matches today's date
                    lines.add(line); // Keep the line if the date matches
                }
            }
            reader.close();

            // Rewrite the file with only today's logs
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String remainingLine : lines) {
                writer.write(remainingLine);
                writer.newLine();
            }
            writer.close();
            
            System.out.println("Logs from the current date have been kept, and the rest have been deleted.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
