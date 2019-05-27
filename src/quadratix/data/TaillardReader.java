package quadratix.data;

import javafx.util.Pair;

import java.io.*;
import java.util.Arrays;

public class TaillardReader {

    private static final String TAILLARD_PATH = "./res/taillard";
    private File file;
    private BufferedReader bufferedReader;

    public TaillardReader(String filename) throws FileNotFoundException {
        this.file = new File(filename);
        FileReader fileReader = new FileReader(TAILLARD_PATH + "\\" + filename);
        this.bufferedReader = new BufferedReader(fileReader);
    }

    /**
     * Extract assignement quadratix.data from taillard instances
     *
     * @return AssignmentData object with taillard's given quadratix.data
     * @throws IOException Throw when an error occurred while reading the file.
     */
    public AssignmentData createAssignementData() throws IOException {
        AssignmentData assignmentData = new AssignmentData();

        String line;
        int spaceCounter = 0;
        int lineCounter = 1;

        while ((line = this.bufferedReader.readLine()) != null) {
            lineCounter++;
            String[] lineContent = line.trim().split("\\s+");

            if (lineContent.length == 1 && !lineContent[0].equals("")) {
                int length = Integer.parseInt(lineContent[0]);
                if (length == 0)
                    throw new IllegalArgumentException("Length argument cannot be equals to 0");

                assignmentData.setLength(length);
            } else if (lineContent.length > 1) {
                if (assignmentData.getLength() == null)
                    throw new AssignmentDataException("No length found in Taillard file");

                for (int i = 0; i < assignmentData.getLength(); i++) {
                    int value;
                    try {
                        value = Integer.parseInt(lineContent[i]);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        throw new AssignmentDataException("Number of arguments exceed length");
                    }

                    if (spaceCounter == 1) {
                        assignmentData.addDistance(new Pair<>((long) lineCounter, (long) i+1), (long) value);

                    } else {
                        assignmentData.addWeight(new Pair<>((long) lineCounter, (long) i+1), (long) value);

                    }
                }
            } else {
                spaceCounter++;
                lineCounter = 0;
            }
        }

        System.out.print("->");

        //CHECK DATA
        if (assignmentData.getDistances().isEmpty())
            throw new AssignmentDataException("Taillard's distances must be informed");

        if (assignmentData.getWeights().isEmpty())
            throw new AssignmentDataException("Taillard's weights must be informed");

        if (assignmentData.getDistances().size() != assignmentData.getWeights().size())
            throw new AssignmentDataException("Taillard's distances vector and weights vector must have same length");

        return assignmentData;
    }
}