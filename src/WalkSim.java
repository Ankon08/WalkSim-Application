/**
 * WalkSim is a Java program that simulates different types of walks (random walk, spiral walk, and breadcrumb walk)
 * based on user input and generates an animation of the walk path.*
 * The program prompts the user to input the number of steps and the type of walker.
 * For random walk and breadcrumb walk, the user is also asked to provide a file containing a FloatMatrix.
 * The walk paths are then simulated, saved to an output file, and visualized using the WalkFrame class.
 *
 * @author Ankon Biswas
 * @id     B00915283
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class WalkSim {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Prompt the user to enter the number of steps
        int nSteps = numSteps(in);

        // Prompt the user to select the type of walker (random, spiral, or breadcrumb)
        int walkType = walkerType(in);

        // Initialize a variable to store the file name for FloatMatrix
        String fileName = "";

        // For random walk and breadcrumb walk, prompt the user to enter the file name for FloatMatrix
        if (walkType == 0 || walkType == 2) {
            fileName = readFileName(in, "Enter the file name for FloatMatrix: ");
        }

        // Prompt the user to enter the output file name
        String outputFile = readFileName(in, "Enter the output file name: ");

        // Define cardinal directions for the random walk
        String[] cardinals = {"N", "E", "S", "W"};
        FloatMatrix T1 = null;

        try {
            // If the walker type is random walk or breadcrumb walk, read the FloatMatrix from the specified file
            if (walkType == 0 || walkType == 2) {
                // Check if the file name for FloatMatrix is provided
                if (!fileName.isEmpty()) {
                    // Read the FloatMatrix from the file
                    T1 = FloatMatrix.fromFile(fileName);

                    // Ensure that the Walker MarkovChain has 4 states
                    assert T1.rows() == 4 : "Walker MarkovChain should have 4 states";
                    System.out.println(T1.prettyString());

                    // If the walker type is random walk, create a MarkovChain and RandomWalker
                    if (walkType == 0) {
                        MarkovChain mc = new MarkovChain(T1, cardinals);
                        RandomWalker walker = new RandomWalker(mc);
                        ArrayList<Coordinate> theWalk = walker.walk(nSteps);
                        walker.saveWalkToFile(outputFile);

                        // Visualize the walk path using WalkFrame
                        WalkFrame walkFrame = new WalkFrame();
                        walkFrame.animatePath(theWalk, 30);
                    }
                    // If the walker type is breadcrumb walk, create a BreadCrumbWalker
                    else {
                        BreadCrumbWalker breadCrumbWalker = new BreadCrumbWalker(new MarkovChain(T1, cardinals));
                        ArrayList<Coordinate> theWalk = breadCrumbWalker.walk(nSteps);
                        breadCrumbWalker.saveWalkToFile(outputFile);

                        // Visualize the walk path using WalkFrame
                        WalkFrame walkFrame = new WalkFrame();
                        walkFrame.animatePath(theWalk, 30);
                    }

                } else {
                    // If the file name for FloatMatrix is empty, print an error message and exit the program
                    System.out.println("File name for FloatMatrix is empty. Exiting program.");
                    System.exit(1);
                }
            }
            // If the walker type is spiral walk, create a SpiralWalker
            else if (walkType == 1) {
                SpiralWalker spiralWalker = new SpiralWalker();
                ArrayList<Coordinate> theWalk = spiralWalker.walk(nSteps);
                spiralWalker.saveWalkToFile(outputFile);

                // Visualize the walk path using WalkFrame
                WalkFrame walkFrame = new WalkFrame();
                walkFrame.animatePath(theWalk, 30);
            }
            // If the walker type is invalid, print an error message and exit the program
            else {
                System.out.println("Invalid walker type. Exiting program.");
                System.exit(1);
            }

        } catch (FileNotFoundException fnfe) {
            // If a file is not found, print an error message and exit the program
            System.out.println("Could not find the specified matrix file.");
            System.out.println(fnfe.toString());
            System.exit(1);
        } catch (IOException ioe) {
            // If an IOException occurs, print an error message and exit the program
            System.out.println("Could not save walk to file: " + outputFile);
            System.out.println(ioe.getMessage());
            System.exit(1);
        }
    }

    /**
     * A utility method to prompt the user for the number of steps in the walk.
     *
     * @param in Scanner for user input
     * @return The number of steps entered by the user
     */
    private static int numSteps(Scanner in) {
        int result = 0;
        boolean isValidInput = false;

        // Continue prompting the user until a valid input is provided
        while (!isValidInput) {
            System.out.print("Enter the number of steps: ");
            // Check if the next input is an integer
            if (in.hasNextInt()) {
                result = in.nextInt();
                // Check if the entered integer is positive
                if (result > 0) {
                    isValidInput = true;
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } else {
                // If the input is not an integer, print an error message and consume the invalid input
                System.out.println("Invalid input. Please enter a valid integer.");
                in.next();
            }
        }

        return result;
    }

    /**
     * A utility method to prompt the user for the type of walker (random, spiral, or breadcrumb).
     *
     * @param in Scanner for user input
     * @return The selected walker type (0 for Random, 1 for Spiral, 2 for Bread crumb)
     */
    private static int walkerType(Scanner in) {
        int walkerType = -1;

        // Continue prompting the user until a valid walker type is provided
        while (walkerType < 0 || walkerType > 2) {
            System.out.print("Enter the walker type (0 for Random, 1 for Spiral, 2 for Bread crumb): ");
            // Check if the next input is an integer
            if (in.hasNextInt()) {
                walkerType = in.nextInt();
            } else {
                // If the input is not an integer, print an error message and consume the invalid input
                System.out.println("Invalid input. Please enter a valid walker type (0, 1, or 2).");
                in.next();
            }
        }

        return walkerType;
    }

    /**
     * A utility method to prompt the user for a non-empty file name.
     *
     * @param in     Scanner for user input
     * @param prompt Prompt message to display to the user
     * @return The entered file name
     */
    private static String readFileName(Scanner in, String prompt) {
        String fileName = "";

        // Continue prompting the user until a non-empty file name is provided
        while (fileName.trim().isEmpty()) {
            System.out.print(prompt);
            fileName = in.next().trim();
        }

        return fileName;
    }
}
