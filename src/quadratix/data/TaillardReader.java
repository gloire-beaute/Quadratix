package quadratix.data;

import javafx.util.Pair;

import java.io.*;

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
     * @throws IOException
     */
    public AssignmentData createAssignementData() throws IOException {
        AssignmentData assignmentData = new AssignmentData();

        String line;
        int spaceCounter = 0;
        int lineCounter = 1;
        System.out.print("Processing\n");

        while ((line = this.bufferedReader.readLine()) != null) {
            lineCounter++;
            //Progress bar
            System.out.print("-");

            //Processing
            String[] lineContent = line.trim().split("\\s+");

            if (lineContent.length == 1 && !lineContent[0].equals("")) {
                int length = Integer.parseInt(lineContent[0]);
                if (length == 0)
                    throw new IllegalArgumentException("Length argument cannot be equals to 0");

                assignmentData.setLength(length);
            } else if (lineContent.length > 1) {
                if (assignmentData.getLength() == null)
                    throw new AssignmentDataException("No length found in Taillard file");

                //TODO optimize for loop (starting with lincounter because we transpose left side)
                for (int i = 0; i < assignmentData.getLength(); i++) {
                    int value;
                    try {
                        value = Integer.parseInt(lineContent[i]);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        throw new AssignmentDataException("Number of arguments exceed length");
                    }

                    if (spaceCounter == 1) {
                        assignmentData.addDistance(new Pair<Long, Long>((long) lineCounter, (long) i), (long) value);

                    } else {
                        assignmentData.addWeight(new Pair<Long, Long>((long) lineCounter, (long) i), (long) value);

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