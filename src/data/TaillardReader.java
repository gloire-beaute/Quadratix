package data;

import javafx.util.Pair;

import java.io.*;
import java.util.Arrays;

public class TaillardReader {

    private static String TAILLARD_PATH = "./resources";
    private File file;
    private BufferedReader bufferedReader;

    public TaillardReader(String filename) throws FileNotFoundException {
        this.file = new File(filename);
        FileReader fileReader = new FileReader(TAILLARD_PATH + "\\" + filename);
        this.bufferedReader = new BufferedReader(fileReader);
    }

    /**
     * Extract assignement data from taillard instances
     *
     * @return AssignementData object with taillard's given data
     * @throws IOException
     */
    public AssignementData createAssignementData() throws IOException {
        AssignementData assignementData = new AssignementData();

        String line;
        int spaceCounter = 0;
        int lineCounter = 0;
        System.out.print("Processing\n");

        while ((line = this.bufferedReader.readLine()) != null) {
            //Progress bar
            System.out.print("-");

            //Processing
            String[] lineContent = line.trim().split("\\s+");

            if (lineContent.length == 1 && !lineContent[0].equals("")) {
                int length = Integer.parseInt(lineContent[0]);
                if (length == 0)
                    throw new IllegalArgumentException("Length argument cannot be equals to 0");

                assignementData.setLength(length);
            } else if (lineContent.length > 1) {
                if (assignementData.getLength() == null)
                    throw new TaillardException("No length found in Taillard file");

                //TODO optimize for loop (starting with lincounter because we transpose left side)
                for (int i = 0; i < assignementData.getLength(); i++) {
                    int value;
                    try {
                        value = Integer.parseInt(lineContent[i]);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        throw new TaillardException("Number of arguments exceed length");
                    }

                    if (spaceCounter == 1) {
                        assignementData.addWeight(new Pair<>(lineCounter, i), value);
                    } else {
                        assignementData.addDistance(new Pair<>(lineCounter, i), value);
                    }
                }
            } else {
                spaceCounter++;
                lineCounter = 0;
            }
        }

        System.out.print("->");

        //CHECK DATA
        if (assignementData.getDistances().isEmpty())
            throw new TaillardException("Taillard's distances must be informed");

        if (assignementData.getWeights().isEmpty())
            throw new TaillardException("Taillard's weights must be informed");

        if (assignementData.getDistances().size() != assignementData.getWeights().size())
            throw new TaillardException("Taillard's distances vector and weights vector must have same length");

        return assignementData;
    }
}