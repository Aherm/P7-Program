package visual;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

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
