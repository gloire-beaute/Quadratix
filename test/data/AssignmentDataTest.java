package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.assignement.AssignmentData;

class AssignmentDataTest {

    private AssignmentData assignementData = new AssignmentData();

    @BeforeEach
    void setup() {

    }

    @Test
    public void print() {
        System.out.println(assignementData.toString());
    }

}