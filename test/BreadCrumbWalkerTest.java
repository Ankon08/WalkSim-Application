/**
 * Test class for the BreadCrumbWalker class, which performs a random walk and then walks back along the same
 * path in reverse order. The class includes tests for the walk method and the saveWalkToFile method.
 * Author : Ankon Biswas
 * ID     : B00915283
 */
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class BreadCrumbWalkerTest {

    // Defined a simple 4x4 matrix for creating a MarkovChain used in tests
    static final float[] testMat1 = {0.25f, 0.25f, 0.25f, 0.25f,
                                     0.25f, 0.25f, 0.25f, 0.25f,
                                     0.25f, 0.25f, 0.25f, 0.25f,
                                     0.25f, 0.25f, 0.25f, 0.25f};
    static final MarkovChain testMC1 = new MarkovChain(new FloatMatrix(testMat1, 4));

    /**
     * Unit test for the walk method in BreadCrumbWalker.
     * It checks the behavior of the method for a common scenario with a positive number of steps.
     */
    @Test
    void walk_testCommon() {
        BreadCrumbWalker walker = new BreadCrumbWalker(testMC1);
        int Nsteps = 10;

        ArrayList<Coordinate> path = walker.walk(Nsteps);

        assertNotNull(path);
        assertEquals(path.size(), 2 * Nsteps + 1, "With N steps, expected 2N+1 points on the path");
    }

    /**
     * Unit test for the walk method in BreadCrumbWalker.
     * It checks the behavior of the method when the number of steps is zero.
     */
    @Test
    void walk_testZeroSteps() {
        BreadCrumbWalker walker = new BreadCrumbWalker(testMC1);
        int Nsteps = 0;

        ArrayList<Coordinate> path = walker.walk(Nsteps);

        assertNotNull(path);
        assertEquals(path.size(), 0, "Expected empty path with zero steps.");
    }

    /**
     * Unit test for the walk method in BreadCrumbWalker.
     * It checks the behavior of the method when the number of steps is negative.
     */
    @Test
    void walk_testNegSteps() {
        BreadCrumbWalker walker = new BreadCrumbWalker(testMC1);
        int Nsteps = -1;

        ArrayList<Coordinate> path = walker.walk(Nsteps);

        assertNotNull(path);
        assertEquals(path.size(), 0, "Expected empty path with negative steps.");
    }

    /**
     * Unit test for the common saveWalkToFile method in the BreadCrumbWalker class.
     * It checks the behavior of the method when saving the walk to both a .txt file and a .dat file.
     */
    @Test
    void saveWalkToFile_Common() {
        BreadCrumbWalker walker = new BreadCrumbWalker(testMC1);
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
     * Unit test for the saveWalkToFile method in BreadCrumbWalker.
     * It checks the behavior of the method when attempting to save the walk to a non-existent directory.
     */
    @Test
    void saveWalkToFile_Exception() {
        BreadCrumbWalker walker = new BreadCrumbWalker(testMC1);
        String fakePath = "foobarbaddirectory" + File.separator
                + "definitelynotarealdirectory123905" + File.separator
                + "testFile.txt";
        try {
            // Act: Attempt to save the walk to a non-existent directory
            walker.saveWalkToFile(fakePath);

            // Assert: Fail the test as an exception is expected
            fail("Should have failed trying to write to the fake path: " + fakePath);
        } catch (IOException e) {
            // Exception expected
        }
    }
}
