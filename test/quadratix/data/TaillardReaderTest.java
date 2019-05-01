package quadratix.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class TaillardReaderTest {

    @Test
    void createAssignementData() {
        try {
            TaillardReader taillardReader = new TaillardReader("tai12.txt");
            AssignmentData assignmentData = taillardReader.createAssignementData();
            System.out.println(assignmentData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}