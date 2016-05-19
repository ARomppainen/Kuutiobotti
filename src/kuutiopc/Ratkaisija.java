package kuutiopc;

import java.util.ArrayList;

public class Ratkaisija {

    public static final double INFINITY = 20;
    //private KuutioDAO dao;
    
    private Trimmer trimmer;

    public Ratkaisija() {
        //dao = null;
        trimmer = new Trimmer();
    }

    public ArrayList<String> ratkaiseKuutio(Kuutio kuutio) {
        Kuutio k = new Kuutio(kuutio);
        
        ArrayList<String> ratkaisu = new ArrayList<>();
        ArrayList<String> vali;
        
        
        for (int STAGE = 8; STAGE <= 8; STAGE++) {
            vali = IDAStar(k, STAGE);

            if (vali != null && !vali.isEmpty()) {
                k.applySolution(vali);
                
                for (String s : vali) {
                    ratkaisu.add(s);
                }
                
                vali.clear();
                
                System.out.println("Stage "+ STAGE + " completed.\n");
            } else {
                System.out.println("WTF?!?!?!?\n");
            }
        }
        
        return trimmer.trim(ratkaisu);
    }

    /*
    public double calcHeuristic(Kuutio k, int round) {
        if (dao != null) {
            int luku = dao.get(k.getCode(round), round);
            
            if (luku != -1) {
                good++;
                return luku;
            }
        }
        
        bad++;
        return k.getHeuristic(round);
    }

    public boolean goalFound(Kuutio k, int round) {
        return k.goalFound(round, dao);
    }
    */

    // http://en.wikipedia.org/wiki/IDA*
    // Muokattu IDAStar, lisätty round
    public ArrayList<String> IDAStar(Kuutio k, int stage) {
        //double costLimit = calcHeuristic(k, round);
        double costLimit = k.getHeuristic(stage);

        if (costLimit == 0) {
            return null;
        }
        
        /*
        if (stage == 7) {
            costLimit += 2;
        }
        */
            
        Path solution;

        while (true) {
            ArrayList<Kuutio> path = new ArrayList<Kuutio>();

            System.out.println("Searching with cost limit: " + costLimit);

            solution = DFS(0, k, costLimit, path, stage);
            costLimit = solution.getCost();

            if (!solution.isEmpty()) {
                //System.out.println("Good: " + good + " Bad: " + bad);
                //System.out.printf("good / bad = %.3f\n", good / ((double) bad));
                return trimmer.trimSolution(solution.getPath());
            } else if (costLimit == INFINITY) {
                return null;
            }
        }
    }

    // Muokattu DFS, lisätty round
    private Path DFS(double startCost, Kuutio k, double costLimit, ArrayList<Kuutio> pathSoFar, int stage) {
        //double minimumCost = startCost + calcHeuristic(k, round);
        double minimumCost = startCost + k.getHeuristic(stage);

        //System.out.println("Searching...");

        if (minimumCost > costLimit) {
            //System.out.println("cost > limit");
            return new Path(minimumCost);
        }

        if (k.goalFound(stage)) {
            return new Path(pathSoFar, costLimit);
        }

        double nextCostLimit = INFINITY;

        for (Kuutio k2 : k.getNeighbors(stage)) {
            //System.out.println("Iterating node: "+ s);

            double newStartCost = startCost + 1; // alkuhinta + etäisyys(alku, s)
            pathSoFar.add(k2);
            Path p = DFS(newStartCost, k2, costLimit, pathSoFar, stage);
            double newCostLimit = p.getCost();

            if (!p.isEmpty()) {
                //System.out.println("Path found!");
                return p;
            }

            pathSoFar.remove(pathSoFar.size() - 1); // tämä pitää poistaa
            nextCostLimit = Math.min(nextCostLimit, newCostLimit);
        }

        return new Path(nextCostLimit);
    }

    private class Path {

        private ArrayList<Kuutio> path;
        private double cost;

        public Path(double cost) {
            path = new ArrayList<>();
            this.cost = cost;
        }

        public Path(ArrayList<Kuutio> path, double cost) {
            this.path = new ArrayList<>(path);
            this.cost = cost;
        }

        public ArrayList<Kuutio> getPath() {
            return path;
        }

        public double getCost() {
            return cost;
        }

        public boolean isEmpty() {
            return path.isEmpty();
        }
    }
}
