import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private static class Node implements Comparable<Node> {
        Node parent;
        Board board;
        int backwards;
        int priority;

        public Node(Node parent, Board board, int backwards) {
            this.parent = parent;
            this.board = board;
            this.backwards = backwards;
            this.priority = backwards + board.manhattan();
        }

        @Override
        public int compareTo(Node n) {
            if (priority < n.priority) return -1;
            if (priority > n.priority) return 1;
            return Integer.compare(n.backwards, backwards);
        }
    }

    private Iterable<Board> solver;
    private int size;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("constructor Solver argument is null!");
        }

        Node isGoal = aStartSearchInLockstep(initial, initial.twin());
        pathExtract(isGoal, initial);
    }

    private void pathExtract(Node isGoal, Board initial) {
        size = -1;
        if (isGoal != null) {
            Node parent = isGoal;
            Stack<Board> s = new Stack<>();

            while (parent.parent != null) {
                s.push(parent.board);
                parent = parent.parent;
            }

            if (parent.board.equals(initial)) {
                s.push(parent.board);
                size = s.size() - 1;

                solver = s;
            }
        }
    }

    private boolean isDuplicate(Node parent, Board neighbor) {
        return parent.parent != null && neighbor.equals(parent.parent.board);
    }

    private Node aStartSearchInLockstep(Board initial, Board twin) {
        MinPQ<Node> frontier = new MinPQ<>();
        frontier.insert(new Node(null, initial, 0));
        frontier.insert(new Node(null, twin, 0));

        Node isGoal = null;
        while (!frontier.isEmpty()) {
            Node min = frontier.delMin();
            if (min.board.isGoal()) {
                isGoal = min;
                break;
            }

            for (Board neighbor : min.board.neighbors()) {
                if (!isDuplicate(min, neighbor)) {
                    frontier.insert(new Node(min, neighbor, min.backwards + 1));
                }
            }
        }

        return isGoal;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return size != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return size;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solver;
    }

    // test client (see below) 
    public static void main(String[] args) {

    }

}
