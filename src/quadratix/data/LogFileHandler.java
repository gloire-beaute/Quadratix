package quadratix.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LogFileHandler {
    private static final String LOG_PATH = "./res/logs";

    private BufferedWriter bufferedWriter;

    public LogFileHandler(int combinationSize) throws IOException {
        File file = new File(LOG_PATH + "\\log" + combinationSize);
        if ((!file.exists() && file.createNewFile()) || file.exists()) {
            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
        }
    }

    public void writeLogs(ArrayList<Integer> data) throws IOException {
        for (Integer datum : data) {
            this.bufferedWriter.write(datum.toString());
            this.bufferedWriter.newLine();
        }
        this.bufferedWriter.close();

    }
}
