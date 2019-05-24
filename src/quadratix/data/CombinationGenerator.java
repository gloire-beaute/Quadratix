package quadratix.data;

import quadratix.combination.Combination;

import java.io.*;
import java.util.ArrayList;

public class CombinationGenerator {
    private static final String COMBINATION_PATH = "./res/combination";
    private static final int NUMBER_COMBINATION = 100;

    private int combinationSize;
    private File file;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public CombinationGenerator(int combinationSize) throws IOException {
        this.combinationSize = combinationSize;
        String filename = "comb_" + combinationSize + ".txt";
        this.file = new File(COMBINATION_PATH + "\\" + filename);
        if ((!file.exists() && file.createNewFile()) || file.exists()) {
            this.bufferedReader = new BufferedReader(new FileReader(file));
            this.bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        }
    }

    public void generateFile() throws IOException {
        for (int i = 0; i < NUMBER_COMBINATION; i++) {
            this.bufferedWriter.write(String.valueOf(Combination.generateRandom(combinationSize)));
            this.bufferedWriter.newLine();
        }

        this.bufferedWriter.close();

    }

    public ArrayList<Combination> readFile() throws IOException {
        if((this.bufferedReader.readLine()) == null){
            this.generateFile();
        }
        ArrayList<Combination> initialValues = new ArrayList<>();

        String line;

        while ((line = this.bufferedReader.readLine()) != null) {
            String[] lineContent = line.trim().split(",");

            if (!lineContent[0].equals("")) {
                Combination combination = new Combination();
                for (int i = 0; i < lineContent.length; i++) {
                    combination.add(Long.valueOf(lineContent[i].replace(" ", "")));
                }
                initialValues.add(combination);
            }

        }

        return initialValues;
    }
}
