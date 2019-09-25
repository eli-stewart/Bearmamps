import java.util.Comparator;
import java.util.HashMap;

// Node Class
public class Node {
    private long id;
    private double lon;
    private double lat;
    private HashMap<Long, GraphDB.Edge> edges = new HashMap<>();
    private String name;

    public long getId() {
        return id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public HashMap<Long, GraphDB.Edge> getEdges() {
        return edges;
    }

    public String getName() {
        return name;
    }

    public Node(long id, double lon, double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public void addName(String n) {
        name = n;
    }

    public void addEdge(GraphDB.Edge e) {
        if (id != e.getNode1ID() && id != e.getNode2ID()) {
            System.out.println("This Edge does not contain this Node");
            return;
        }

        if (e.getNode1ID() == id) {
            edges.put(e.getNode2ID(), e);
        } else {
            edges.put(e.getNode1ID(), e);
        }
    }

    public static Comparator<Node> getXCOMPARATOR() {
        return XCOMPARATOR;
    }

    private static Comparator<Node> XCOMPARATOR = new Comparator<Node>() {
        @Override
        public int compare(Node n1, Node n2) {
            double n1X = GraphDB.projectToX(n1.lon, n1.lat);
            double n2X = GraphDB.projectToX(n2.lon, n2.lat);

            return Double.compare(n1X, n2X);
        }
    };

    public static Comparator<Node> getYCOMPARATOR() {
        return YCOMPARATOR;
    }

    private static Comparator<Node> YCOMPARATOR = new Comparator<Node>() {
        @Override
        public int compare(Node n1, Node n2) {
            double n1Y = GraphDB.projectToY(n1.lon, n1.lat);
            double n2Y = GraphDB.projectToY(n2.lon, n2.lat);

            return Double.compare(n1Y, n2Y);
        }
    };
}
