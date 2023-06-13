import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int[][] board;
    private Board twin;
    private final int size;
    private Node blank;
    private int manhattan; // heuristic
    private int hamming; // heuristic

    private static class Node {
        private final int x;
        private final int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        twin = null;
        size = tiles.length;
        manhattan = 0;
        hamming = 0;

        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    blank = new Node(i, j);
                } else if (tiles[i][j] != convertToNum(i, j)) {
                    Node node = convertToCoordinate(tiles[i][j]);
                    manhattan += Math.abs(i - node.x) + Math.abs(j - node.y);
                    hamming++;
                }
                board[i][j] = tiles[i][j];
            }
        }

    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder(size + "\n");
        for (int i = 0; i < size; i++) {
            StringBuilder row = new StringBuilder();
            if (board[i][0] <= 9) {
                row.append(" ").append(board[i][0]);
            } else {
                row.append(board[i][0]);
            }
            for (int j = 1; j < size; j++) {
                if (board[i][j] > 9) {
                    row.append(" ").append(board[i][j]);
                } else {
                    row.append("  ").append(board[i][j]);
                }

            }
            str.append(row).append("\n");
        }

        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // convert (i, j) to ordered number e.g. (0, 0) -> 1, (0, 1) -> 2
    private int convertToNum(int i, int j) {
        return i * size + j + 1;
    }

    // convert number to right (i, j)
    private Node convertToCoordinate(int num) {
        return new Node((num - 1) / size, (num - 1) % size);
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i * size + j + 1 > size * size - 1) {
                    break;
                }
                if (board[i][j] != convertToNum(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y != null && y.getClass() == this.getClass()) {
            if (size != ((Board) y).size) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != ((Board) y).board[i][j]) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> next = new Queue<>();
        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};
        int[][] b = new int[size][size];
        for (int m = 0; m < size; m++) {
            b[m] = Arrays.copyOf(board[m], size);
        }

        for (int i = 0; i < 4; i++) {
            int newx = blank.x + dx[i];
            int newy = blank.y + dy[i];
            if (newx >= 0 && newx < size && newy >= 0 && newy < size) {
                swap2D(b, blank.x, blank.y, newx, newy);
                next.enqueue(new Board(b));
                swap2D(b, blank.x, blank.y, newx, newy);
            }
        }

        return next;
    }

    private void swap2D(int[][] array, int x, int y, int newx, int newy) {
        int temp = array[x][y];
        array[x][y] = array[newx][newy];
        array[newx][newy] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin != null) {
            return twin;
        }

        int newx = blank.x;
        int newy = blank.y;
        int x = 0;
        int y = 0;
        while ((blank.x == newx && blank.y == newy) || (blank.x == x && blank.y == y)) {
            x = StdRandom.uniformInt(size);
            y = StdRandom.uniformInt(size - 1);

            newx = x;
            newy = y + 1;
        }

        swap2D(board, x, y, newx, newy);
        twin = new Board(board);
        swap2D(board, x, y, newx, newy);

        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}