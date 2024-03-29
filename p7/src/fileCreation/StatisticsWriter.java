package fileCreation;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticsWriter {

    public static void writeFile(String output) throws IOException {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(date);
        output += "-------------------------------------------" +
                "\r\n|" + "Created on: " + date + "|\r\n" +
                "-------------------------------------------";
        FileWriter fw = new FileWriter("./statistics/DataAnalysis " + dateString + ".txt");
        fw.write(output);
        fw.close();
    }
}