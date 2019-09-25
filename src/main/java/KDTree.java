import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class KDTree {

    private KDNode root;
    private GraphDB g;

    public KDNode getRoot() {
        return root;
    }

    public GraphDB getG() {
        return g;
    }

    public KDTree(GraphDB g) {
        this.g = g;
        HashMap<Long, Node> n = g.getNodes();
        ArrayList<Node> nodes = new ArrayList<>(n.values());
        root = builder(nodes, true);
    }

    public KDNode builder(ArrayList<Node> n, boolean x) {
        if (x) {
            Collections.sort(n, Node.getXCOMPARATOR());
        } else {
            Collections.sort(n, Node.getYCOMPARATOR());
        }

        if (n.isEmpty()) {
            return null;
        }
        if (n.size() == 1) {
            return new KDNode(n.get(0).getId(), x, null, null, this.g);
        }

        int medIndx = n.size() / 2;
        Node median = n.get(medIndx);

        KDNode lessChild = builder(new ArrayList<>(n.subList(0, medIndx)), !x);
        KDNode greatChild = builder(new ArrayList<>(n.subList(medIndx + 1, n.size())), !x);

        return new KDNode(median.getId(), x, lessChild, greatChild, this.g);
    }
}
