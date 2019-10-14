package util;

import enums.RentType;
import java.io.*;
import java.nio.channels.Channels;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.glassfish.hk2.utilities.reflection.Logger;

/**
 * @author Sebastian Schiefermayr
 */
public class SystemUtil {
    /** 
     * @param title - Filename
     * @param message - e.g. Sebastian Schiefermayr - Panasonic GH4
     * @param type - [AUSBORGEN] | [RESERVIEREN] | [RETOUR]  
     * Log Format: 07.10.2019 [AUSBORGEN] Sebastian Schiefermayr - Panasonic GH4
     */
    public static void logToFile(String title, String message, RentType type) {
        String logDirPath = "log";
        // Necessary Files
        File logDir = new File(logDirPath);
        File logFile = new File(logDirPath + "/" + title.replace(" ", "_") + ".txt");
        // For the Log timestamp
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        // Append it with the message to the final log String
        String logMessage = dtf.format(now) + " [" + type.toString() + "] " + message;
        if (!logDir.exists()) {
            logDir.mkdir();
            
        }
        System.out.println("Log Ordner: " + logDir.getAbsolutePath());
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Create a Writer and append the message in the log file
        try (Writer writer = Channels.newWriter(new FileOutputStream(
                logFile.getAbsoluteFile(), true).getChannel(), "UTF-8")) {
            writer.append(logMessage + "\n");
        } catch (FileNotFoundException e) {
            Logger.getLogger().warning("Log File not found");
        } catch (IOException e) {
            Logger.getLogger().warning("An IO Exception occurred");
            e.printStackTrace();
        }
    }
}