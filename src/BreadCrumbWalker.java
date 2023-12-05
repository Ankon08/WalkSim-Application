/**
 * BreadCrumbWalker is a class representing a walker that performs a random walk and then walks back
 * along the same path in reverse order. It uses a Markov chain to determine the directions of the walk.
 * The resulting path can be saved to a file in different formats (integer stream or coordinates).
 *
 * @author Ankon Biswas
 * @id     B00915283
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A class encoding a walker that performs a random walk and then walks back along the same path in reverse order.
 */
public class BreadCrumbWalker {

    // Constants defining the starting position and step size
    public static final int START_X = 0;
    public static final int START_Y = 0;
    public static final int STEP_SIZE = 1; // each step along the lattice moves this many units

    private MarkovChain mc; // Markov chain to determine walk direction
    private Coordinate curPos; // Current position of the walker
    protected ArrayList<Coordinate> path; // List to store the path of the walker

    /**
     * Constructs a BreadCrumbWalker from a given Markov chain.
     * The Markov chain should at most have the state labels "N", "E", "S", "W,"
     * which encode the cardinal directions to take.
     *
     * @param chain the Markov chain encoding walk direction
     */
    public BreadCrumbWalker(MarkovChain chain) {
        mc = chain;
        curPos = new Coordinate(START_X, START_Y);
        path = new ArrayList<>();
    }

    /**
     * Performs a bread crumb walk of N steps (forward and backward) and returns the resulting path.
     * The path is represented as a list of coordinates indicating the start and end of each step.
     * Specifically, the coordinate at index i and the coordinate at index i+1 encode the i+1'th step
     * from coordinate i to coordinate i+1.
     *
     * If N steps are 0 or negative, the resulting path is empty.
     *
     * Note that each call to walk resets the path of this walker to start again from the default
     * starting point and clears the history of the previous path.
     *
     * @param Nsteps the number of steps to simulate in the bread crumb walk
     * @return the path of the bread crumb walk.
     */
    public ArrayList<Coordinate> walk(int Nsteps) {
        int N = Nsteps;
        path.clear(); // Reset the path for this new walk.

        // If there are positive steps, add the starting position to the path
        if (N > 0) {
            path.add(new Coordinate(curPos.x, curPos.y));
        }
        // Perform the forward walk
        for (int step = 0; step < N; ++step) {
            mc.nextState(); // Get the next state from the Markov chain
            curPos.accumulate(getStepDirection()); // Update the current position based on the state
            path.add(new Coordinate(curPos.x, curPos.y)); // Add the new position to the path
        }

        // Perform the backward walk by adding the path in reverse order
        for (int step = N - 1; step >= 0; --step) {
            path.add(new Coordinate(path.get(step).x, path.get(step).y));
        }

        return path;
    }

    /**
     * Given a bread crumb walker, stores its most recently walked path to a file.
     * If the walker's path contains 0 steps, then the resulting file will be empty.
     * Otherwise, for 2N steps, there will be 2N+1 coordinates printed to the file,
     * one per line. Line i and line i+1 encode the i+1'th step beginning at coordinate i
     * and ending at coordinate i+1.
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
     * Private helper method for walk().
     * Given the current state of the Markov chain,
     * computes the "step" based on the direction encoded by the Markov chain.
     *
     * @return a coordinate encoding the difference between the Walker's destination position
     * and its starting position for this step.
     */
    private Coordinate getStepDirection() {
        Coordinate step = new Coordinate(0, 0);
        String state = mc.getStateString();
        if (state.equals("N")) {
            step.accumulate(new Coordinate(0, STEP_SIZE));
        } else if (state.equals("E")) {
            step.accumulate(new Coordinate(STEP_SIZE, 0));
        } else if (state.equals("S")) {
            step.accumulate(new Coordinate(0, -STEP_SIZE));
        } else {
            step.accumulate(new Coordinate(-STEP_SIZE, 0));
        }
        return step;
    }
}

