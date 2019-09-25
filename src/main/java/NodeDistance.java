public class NodeDistance implements Comparable {
    private long id;
    private double dist;
    private long pred;
    private double heuristic;
    private double priority;

    public long getId() {
        return id;
    }

    public double getDist() {
        return dist;
    }

    public long getPred() {
        return pred;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public double getPriority() {
        return priority;
    }

    public NodeDistance(long i, double d, long p, double h) {
        dist = d;
        id = i;
        pred = p;
        heuristic = h;
        priority = (h + d);
    }

    @Override
    public int compareTo(Object o) {
        NodeDistance O = (NodeDistance) o;
        long toReturn = Double.compare(this.priority, O.priority);
        int eturn = (int) toReturn;
        return eturn;
    }
//    @Override
//    public boolean equals(Object obj) {
//        NodeDistance O = (NodeDistance) obj;
//        return this.id == O.id;
//    }
}
