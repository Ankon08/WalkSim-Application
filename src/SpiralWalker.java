/**
 * SpiralWalker is a class that represents a walker moving in a clockwise spiral pattern.
 * It simulates a spiral walk for a specified number of steps, records the path, and allows
 * saving the path to a file in different formats (integer stream or coordinates).
 *
 * @author Ankon Biswas
 * @id     B00915283
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A class encoding a walker which moves in a clockwise spiral.
 */
public class SpiralWalker {

    // Constants defining the starting position and step size
    private static final int START_X = 0;
    private static final int START_Y = 0;
    private static final int STEP_SIZE = 1;
    private Coordinate curPos; // List to store the path of the walker
    private ArrayList<Coordinate> path; // List to store the path of the walker


    /**
     * Constructs a SpiralWalker with the initial position and an empty path.
     */
    public SpiralWalker() {
        curPos = new Coordinate(START_X, START_Y);
        path = new ArrayList<>();
    }

    /**
     * Performs a clockwise spiral walk for a specified number of steps and returns the resulting path.
     * The path is represented as a list of coordinates indicating the start and end of each step.
     *
     * @param Nsteps the number of steps to simulate in the spiral walk
     * @return the path of the walk
     */
    public ArrayList<Coordinate> walk(int Nsteps) {
        int N = Nsteps;
        curPos = new Coordinate(START_X, START_Y);
        path.clear(); // Reset the path for this new walk.

        if (N > 0) {
            addToPath();
        }

        for (int step = 0; step < N; ++step) {
            spiralStep();
            addToPath();
        }

        return path;
    }

    /**
     * Adds the current position to the path.
     * This method creates a new Coordinate object using the current x and y coordinates
     * of the SpiralWalker's position and adds it to the path list.
     * It is a private helper method used internally to update the walker's path during the walk.
     */
    private void addToPath() {
        path.add(new Coordinate(curPos.x, curPos.y));
    }

    /**
     * Saves the walker's path to a specified file in either an integer stream or coordinates format.
     * The file format is determined by the file extension (.dat for integer stream, .txt for coordinates).
     *
     * @param fname the name of the file in which to write the path
     * @throws IOException if the file could not be opened or created for writing
     */
    public void saveWalkToFile(String fname) throws IOException {
        PrintWriter writer = new PrintWriter(fname);

        if (fname.endsWith(".dat")) {
            // Output as integer stream
            for (Coordinate coOrd : path) {
                writer.print(coOrd.x + " " + coOrd.y + " ");
            }
        } else if (fname.endsWith(".txt")) {
            // Output as coordinates
            for (Coordinate coOrd : path) {
                writer.println(String.format("(%d, %d)", coOrd.x, coOrd.y));
            }
        } else {
            // Unsupported file format exception
            throw new IllegalArgumentException("Unsupported file format. Please use .dat or .txt extension.");
        }

        writer.close();
    }

    /**
     * Private helper method representing one step of the spiral walk.
     * Updates the current position based on the clockwise spiral logic.
     */
    private void spiralStep() {
        int spiralLayer = Math.max(Math.abs(curPos.x), Math.abs(curPos.y));

        if (curPos.x == 0 && curPos.y == 0) {
            // Starting point, move up first
            curPos.y += STEP_SIZE;
        } else if (curPos.y == spiralLayer && curPos.x < spiralLayer) {
            curPos.x += STEP_SIZE;  // Move right
        } else if (curPos.x == spiralLayer && curPos.y > -spiralLayer) {
            curPos.y -= STEP_SIZE;  // Move down
        } else if (curPos.y == -spiralLayer && curPos.x > -spiralLayer) {
            curPos.x -= STEP_SIZE;  // Move left
        }  else if (curPos.x == -spiralLayer && curPos.y < spiralLayer-1) {
            // If in the bottom left corner of the layer (adjusted for the desired behavior)
            curPos.y += STEP_SIZE;  // Move up one step
        }else {
            // If in the top left corner of the layer or moving upwards, increase the spiral layer
            spiralLayer++;
            curPos.y = spiralLayer;
        }
    }
}
