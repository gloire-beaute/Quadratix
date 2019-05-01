package quadratix.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class TaillardReaderTest {

    @Test
    void createAssignementData() {
        try {
            TaillardReader taillardReader = new TaillardReader("tai12a.txt");
            AssignementData assignementData = taillardReader.createAssignementData();
            System.out.println(assignementData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}