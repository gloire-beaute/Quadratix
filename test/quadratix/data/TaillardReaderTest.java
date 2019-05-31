package quadratix.data;

import org.junit.jupiter.api.Test;
import quadratix.assignement.AssignmentData;

import java.io.IOException;

class TaillardReaderTest {

    @Test
    void createAssignementData() {
        try {
            TaillardReader taillardReader = new TaillardReader("tai50.txt");
            AssignmentData assignmentData = taillardReader.createAssignementData();
            System.out.println(assignmentData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}