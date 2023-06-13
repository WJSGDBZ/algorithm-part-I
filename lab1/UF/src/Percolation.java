import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private boolean[][] openSite;
    private int nOpenSite;
    private int gridLength;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("index" + n + " is not <= 0");
        }

        gridLength = n;
        openSite = new boolean[n + 1][n + 1];

        uf = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 1; i <= n; i++) {
            // fake top
            uf.union(getFakeTop(), mapp(1, i));
            // fake bottom
            uf.union(getFakeBottom(), mapp(n, i));
        }
    }

    private void validate(int n) {
        if (n <= 0 || n > gridLength) {
            throw new IllegalArgumentException("index" + n + "must between 1 and " + gridLength);
        }
    }

    private int[][] allPossibleSuccessor(int row, int col) {
        int[][] successor = new int[4][2];

        successor[0][0] = row - 1;
        successor[0][1] = col;

        successor[1][0] = row + 1;
        successor[1][1] = col;

        successor[2][0] = row;
        successor[2][1] = col - 1;

        successor[3][0] = row;
        successor[3][1] = col + 1;

        return successor;
    }

    // mapping (row, col) to UF position
    private int mapp(int row, int col) {
        return (row - 1) * gridLength + (col - 1);
    }

    private int getFakeTop() {
        return gridLength * gridLength;
    }

    private int getFakeBottom() {
        return gridLength * gridLength + 1;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row);
        validate(col);

        if (openSite[row][col]) {
            return;
        }

        openSite[row][col] = true;
        nOpenSite++;

        int[][] successor = allPossibleSuccessor(row, col);
        for (int i = 0; i < successor.length; i++) {
            int newrow = successor[i][0];
            int newcol = successor[i][1];

            if (newrow > 0 && newrow <= gridLength && newcol > 0 && newcol <= gridLength) {
                if (openSite[newrow][newcol]) {
                    uf.union(mapp(newrow, newcol), mapp(row, col));
                }
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);

        return openSite[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);

        if (!openSite[row][col]) {
            return false;
        }

        return uf.find(getFakeTop()) == uf.find(mapp(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpenSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(getFakeTop()) == uf.find(getFakeBottom());
    }

    // test client (optional)
    public static void main(String[] args) {

    }

}