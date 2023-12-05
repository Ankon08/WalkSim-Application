import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A class encoding a walker which moves randomly along an integer lattice.
 * The randomness is encoded as a Markov chain encoding
 * which direction the walker should move: North, East, South, West.
 * The Markov chain's states should be a subset of, or equal to, {"N", "E", "S", "W"}.
 */
public class RandomWalker {

    public static final int START_X = 0;
    public static final int START_Y = 0;
    public static final int STEP_SIZE = 1; //each step along the lattice moves this many units

    private MarkovChain mc;
    private Coordinate curPos;
    private ArrayList<Coordinate> path;

    /**
     * Construct a RandomWalker from a Markov chain.
     * The Markov chain should at most have the state labels "N", "E", "S", "W";
     * which encode the cardinal directions to talk
     * @param chain the Markov chain encoding walk direction
     */
    public RandomWalker(MarkovChain chain) {
        mc = chain;
        curPos = new Coordinate(START_X, START_Y);
        path = new ArrayList<>();
    }

    /**
     * Perform a random walk of Nsteps and return the resulting path.
     * The path is encoded as a list of coordinates indicating the start and end of
     * each step. Specifically, the coordinate at index i and the coordinate at index i+1
     * encode the i+1'th step from coordinate i to coordinate i+1.
     *
     * If Nsteps is 0 or negative, the resulting path is empty.
     *
     * Note that each call to walk resets the path of this walker to start again from
     * the default starting point and clears the history of the previous path.
     *
     * @param Nsteps: the number of steps to simulate in the random walk
     * @return the path of the walk.
     */
    public ArrayList<Coordinate> walk(int Nsteps) {
        int N = Nsteps;
        Coordinate curPos = new Coordinate(START_X, START_Y);
        path.clear(); //reset the path for this new walk.

        if (N > 0) {
            path.add(new Coordinate(curPos.x, curPos.y));
        }
        for (int step = 0; step < N; ++step) {
            mc.nextState();
            curPos.accumulate(getStepDirection());
            path.add(new Coordinate(curPos.x, curPos.y));
        }

        return path;
    }

    /**
     * Given a random walker, store its most recently walked path to a file.
     * If the walker's path contains 0 steps, then the resulting file will be empty.
     * Otherwise, for N steps, there will be N+1 coordinates printed to the file,
     * one per line. Line i and line i+1 encode the i+1'th step beginning
     * at coordinate i and ending at coordinate i+1.
     * @param fname the name of the file in which to write the path
     * @throws IOException if the file could not be open or created writing
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
            throw new IllegalArgumentException("Unsupported file format. Please use .dat or .txt extension.");
        }

        writer.close();
    }
    /**
     * Private helper method for walk().
     * Given the current state of the Markov chain,
     * compute the "step" based on the direction encoded by the Markov chain.
     * @return a coordinate encoding the difference between the Walker's destination position
     *         and its starting position for this step.
     */
    private Coordinate getStepDirection() {
        Coordinate step = new Coordinate(0,0);
        String state =  mc.getStateString();
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
