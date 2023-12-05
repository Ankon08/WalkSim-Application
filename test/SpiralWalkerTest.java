/**
 * SpiralWalkerTest - JUnit test class for the SpiralWalker class.
 * Author : Ankon Biswas
 * ID     : B00915283
 */
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SpiralWalkerTest {

    /**
     * Unit test for the walk method in the SpiralWalker class.
     * It checks the common behavior of the walk method by verifying the number of points on the path.
     */
    @Test
    void walk_testCommon() {
        SpiralWalker walker = new SpiralWalker();
        int Nsteps = 10;

        ArrayList<Coordinate> path = walker.walk(Nsteps);

        assertNotNull(path);
        assertEquals(path.size(), Nsteps + 1, "With N steps, expected N+1 points on the path");
    }

    /**
     * Unit test for the walk method in the SpiralWalker class.
     * It checks the behavior of the walk method when zero steps are specified.
     */
    @Test
    void walk_testZeroSteps() {
        SpiralWalker walker = new SpiralWalker();
        int Nsteps = 0;

        ArrayList<Coordinate> path = walker.walk(Nsteps);

        assertNotNull(path);
        assertEquals(path.size(), 0, "Expected empty path with zero steps.");
    }

    /**
     * Unit test for the walk method in the SpiralWalker class.
     * It checks the behavior of the walk method when negative steps are specified.
     */
    @Test
    void walk_testNegSteps() {
        SpiralWalker walker = new SpiralWalker();
        int Nsteps = -1;

        ArrayList<Coordinate> path = walker.walk(Nsteps);

        assertNotNull(path);
        assertEquals(path.size(), 0, "Expected empty path with negative steps.");
    }

    /**
     * Unit test for the common saveWalkToFile method in the SpiralWalker class.
     * It checks the behavior of the method when saving the walk to both a .txt file and a .dat file.
     */
    @Test
    void saveWalkToFile_Common() {
        SpiralWalker walker = new SpiralWalker();
        String pathTxt = "_saveWalkToFile_Common_UnitTest.txt";
        String pathDat = "_saveWalkToFile_Common_UnitTest.dat";

        try {
            // Pre-condition: Ensure that the .txt and .dat files do not exist before saving
            File fpTxt = new File(pathTxt);
            File fpDat = new File(pathDat);
            assertFalse(fpTxt.exists(), "Pre-condition: .txt file should not yet exist.");
            assertFalse(fpDat.exists(), "Pre-condition: .dat file should not yet exist.");

            // Save the walk to both .txt and .dat files
            walker.saveWalkToFile(pathTxt);
            walker.saveWalkToFile(pathDat);

            // Post-condition: Ensure that the .txt and .dat files exist after saving
            assertTrue(fpTxt.exists(), ".txt file should exist after writing to it with no IOException.");
            assertTrue(fpDat.exists(), ".dat file should exist after writing to it with no IOException.");

            // Clean-up: Delete the .txt and .dat files after the test
            assertTrue(fpTxt.delete(), "Expected to be able to delete .txt file after writing to it.");
            assertTrue(fpDat.delete(), "Expected to be able to delete .dat file after writing to it.");
        } catch (IOException e) {
            // Fail the test if an exception occurs during the saveWalkToFile method
            fail("Exception occurred while trying to save walk to the file.");
        }
    }

    /**
     * Tests the behavior of the saveWalkToFile method when an invalid path is provided.
     */
    @Test
    void saveWalkToFile_Exception() {
        SpiralWalker walker = new SpiralWalker();
        String fakePath = "foobarbaddirectory" + File.separator
                + "definitelynotarealdirectory123905" + File.separator
                + "testFile.txt";
        try {
            walker.saveWalkToFile(fakePath);
            fail("Should have failed trying to write to the fake path: " + fakePath);
        } catch (IOException e) {
            // Exception expected
        }
    }
}
