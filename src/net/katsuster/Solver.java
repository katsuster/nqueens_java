package net.katsuster;

public class Solver implements Runnable {
    private int row;
    private int left;
    private int right;
    private int n;
    private int y;
    private long answer;
    private long newAnswer;

    public Solver(int row, int left, int right, int n, int y, long answer) {
        this.row = row;
        this.left = left;
        this.right = right;
        this.n = n;
        this.y = y;
        this.answer = answer;
        this.newAnswer = 0;
    }

    public long getNewAnswer() {
        return newAnswer;
    }

    public void run() {
        newAnswer = solveInner(this.row, this.left, this.right, this.n, this.y, this.answer);
    }

    protected long solveInner(int row, int left, int right, int n, int y, long answer) {
        int field, xb;
        int n_row, n_left, n_right;

        if (y == n) {
            //found the answer
            return answer + 1;
        }

        field = ((1 << n) - 1) & ~(left | row | right);
        while (field != 0) {
            xb = -field & field;
            field = field & ~xb;

            n_row = row | xb;
            n_left = (left | xb) << 1;
            n_right = (right | xb) >> 1;

            //find the next line
            answer = solveInner(n_row, n_left, n_right, n, y + 1, answer);
        }

        return answer;
    }
}
