public class FindNearest {

    private KDNode champion;
    private double xDest;
    private double yDest;

    public FindNearest(double lon, double lat, KDNode root) {
        champion = root;
        xDest = GraphDB.projectToX(lon, lat);
        yDest = GraphDB.projectToY(lon, lat);
    }

    static double euclidean(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public KDNode getChampion() {
        return champion;
    }

    public void findChampion(KDNode node, KDNode parent) {
        double champDist = euclidean(champion.getX(), xDest, champion.getY(), yDest);
        double nodeDist = euclidean(node.getX(), xDest, node.getY(), yDest);
        if (nodeDist < champDist) {
            champion = node;
        }
        double tempDist;
        if (node.isX()) {
            if (node.getX() > xDest) {
                if (node.getLessChild() != null) {
                    findChampion(node.getLessChild(), node);
                }
                if (node.getGreaterChild() == null) {
                    return;
                }
                champDist = euclidean(champion.getX(), xDest, champion.getY(), yDest);
                tempDist = getTempDist1(node, parent);
                if (tempDist < champDist) {
                    findChampion(node.getGreaterChild(), node);
                }
            } else {
                if (node.getGreaterChild() != null) {
                    findChampion(node.getGreaterChild(), node);
                }
                if (node.getLessChild() == null) {
                    return;
                }
                champDist = euclidean(champion.getX(), xDest, champion.getY(), yDest);
                tempDist = getTempDist1(node, parent);
                if (tempDist < champDist) {
                    findChampion(node.getLessChild(), node);
                }
            }
        } else {
            if (node.getY() > yDest) {
                if (node.getLessChild() != null) {
                    findChampion(node.getLessChild(), node);
                }
                if (node.getGreaterChild() == null) {
                    return;
                }
                champDist = euclidean(champion.getX(), xDest, champion.getY(), yDest);
                tempDist = getTempDist2(node, parent);
                if (tempDist < champDist) {
                    findChampion(node.getGreaterChild(), node);
                }
            } else {
                if (node.getGreaterChild() != null) {
                    findChampion(node.getGreaterChild(), node);
                }
                if (node.getLessChild() == null) {
                    return;
                }
                champDist = euclidean(champion.getX(), xDest, champion.getY(), yDest);
                tempDist = getTempDist2(node, parent);
                if (tempDist < champDist) {
                    findChampion(node.getLessChild(), node);
                }
            }
        }
    }

    private double getTempDist1(KDNode node, KDNode parent) {
        double tempDist;
        if (node == parent.getLessChild()) { // if lesser child
            if (yDest > parent.getY()) {
                tempDist = euclidean(xDest, node.getX(), yDest, parent.getY());
            } else {
                tempDist = euclidean(xDest, node.getX(), yDest, yDest);
            }
        } else { // if greater child
            if (yDest < parent.getY()) {
                tempDist = euclidean(xDest, node.getX(), yDest, parent.getY());
            } else {
                tempDist = euclidean(xDest, node.getX(), yDest, yDest);
            }
        }
        return tempDist;
    }


    private double getTempDist2(KDNode node, KDNode parent) {
        double tempDist;
        if (node == parent.getLessChild()) { // if lesser child
            if (xDest > parent.getX()) {
                tempDist = euclidean(xDest, parent.getX(), yDest, node.getY());
            } else {
                tempDist = euclidean(xDest, xDest, yDest, node.getY());
            }
        } else { // if greater child
            if (xDest < parent.getX()) {
                tempDist = euclidean(xDest, parent.getX(), yDest, node.getY());
            } else {
                tempDist = euclidean(xDest, xDest, yDest, node.getY());
            }
        }
        return tempDist;
    }
}
