import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int t;
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n);
        validate(trials);

        results = new double[trials];
        t = trials;
        int ngrid = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation trial = new Percolation(n);
            int open = 0;
            while (!trial.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);

                if (!trial.isOpen(row, col)) {
                    trial.open(row, col);
                    open++;
                }
            }

            results[i] = 1.0 * open / ngrid;
        }
    }

    private void validate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("index" + n + " must >= 0");
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("number of arg must = 2");
        }

        PercolationStats p = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        // PercolationStats p = new PercolationStats(200, 100);
        StdOut.println("mean                    = " + p.mean());
        StdOut.println("stddev                  = " + p.stddev());
        StdOut.println("95% confidence interval = " + "[" + p.confidenceLo() + "," + p.confidenceHi() + "]");
    }
}
