package data;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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