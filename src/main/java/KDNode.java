
public class KDNode {
    private long nodeID;
    private boolean isX;
    private KDNode lessChild;
    private KDNode greaterChild;
    private double x;
    private double y;

    public long getNodeID() {
        return nodeID;
    }

    public boolean isX() {
        return isX;
    }

    public KDNode getLessChild() {
        return lessChild;
    }

    public KDNode getGreaterChild() {
        return greaterChild;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public KDNode(long id, boolean isX, KDNode less, KDNode greater, GraphDB g) {
        nodeID = id;
        this.isX = isX;
        lessChild = less;
        greaterChild = greater;
        x = GraphDB.projectToX(g.getNodes().get(this.nodeID).getLon(),
                g.getNodes().get(this.nodeID).getLat());
        y = GraphDB.projectToY(g.getNodes().get(this.nodeID).getLon(),
                g.getNodes().get(this.nodeID).getLat());
    }
}
