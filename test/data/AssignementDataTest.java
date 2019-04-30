package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignementDataTest {

    private AssignementData assignementData = new AssignementData();

    @BeforeEach
    void setup() {

    }

    @Test
    public void print() {
        System.out.println(assignementData.toString());
    }

}