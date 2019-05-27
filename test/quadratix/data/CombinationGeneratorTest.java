package quadratix.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.combination.Combination;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CombinationGeneratorTest {

    CombinationGenerator combinationGenerator;

    @BeforeEach
    void setup() {
        try {
            combinationGenerator = new CombinationGenerator(12);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateFileTest(){
        try {
            combinationGenerator.generateFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readFileTest(){
        try {
            ArrayList<Combination> combinationArrayList = combinationGenerator.readFile();
            System.out.println(combinationArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}