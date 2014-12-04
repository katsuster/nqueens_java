package net.katsuster;

public class NQueens {
    public static void main(String args[]) {
        int n;
        boolean parallel;
        long answer;
        int center, x, row, left, right;
        long start, elapse;
        Solver[] solvers;
        Thread[] solverThreads;
        Solver solverRemain;
        Thread solverRemainThread;

        if (args.length < 1) {
            System.out.println("usage: \n"
                    + "NQueens queens(1 ... 30) parallel(true or false)");
            return;
        }

        //get the arguments
        n = Integer.valueOf(args[0]);
        if (n < 0 || 30 < n) {
            n = 0;
        }
        System.out.println("queens  : " + n);

        parallel = false;
        if (args.length >= 2) {
            parallel = Boolean.valueOf(args[1]);
        }
        System.out.println("parallel: " + parallel);

        //start
        start = System.nanoTime();

        //solve
        center = n >>> 1;
        solvers = new Solver[center];
        solverThreads = new Thread[center];

        answer = 0;

        //first
        solvers[0] = new Solver(0, 1, 0, n - 1, 0, 0);
        solverThreads[0] = new Thread(solvers[0]);
        if (parallel) {
            solverThreads[0].start();
        } else {
            solverThreads[0].run();
        }

        for (x = 1; x < center; x++) {
            //left half
            row = 1 << x;
            left = row << 1;
            right = row >> 1;

            solvers[x] = new Solver(row, left, right, n, 1, 0);
            solverThreads[x] = new Thread(solvers[x]);
            if (parallel) {
                solverThreads[x].start();
            } else {
                solverThreads[x].run();
            }
        }

        solverRemain = null;
        solverRemainThread = null;
        if (n % 2 == 1) {
            //center(if N is odd)
            row = 1 << center;
            left = row << 1;
            right = row >> 1;

            solverRemain = new Solver(row, left, right, n, 1, 0);
            solverRemainThread = new Thread(solverRemain);
            if (parallel) {
                solverRemainThread.start();
            } else {
                solverRemainThread.run();
            }
        }

        //join
        for (x = 0; x < center; x++) {
            try {
                if (parallel) {
                    solverThreads[x].join();
                }
                answer += solvers[x].getNewAnswer();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        answer *= 2;

        if (solverRemain != null) {
            try {
                if (parallel) {
                    solverRemainThread.join();
                }
                answer += solverRemain.getNewAnswer();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        //finished
        elapse = System.nanoTime() - start;

        //solved
        System.out.println("answers : " + answer);
        System.out.println("time    : " + (elapse / 1000000) + "[ms]");
    }
}
