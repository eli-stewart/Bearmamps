import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.Set;

public class GraphBuildingHandler extends DefaultHandler {

    private static final Set<String> ALLOWED_HIGHWAY_TYPES = Set.of(
            "motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential",
            "living_street", "motorway_link", "trunk_link", "primary_link", "secondary_link",
            "tertiary_link"
    );
    private final GraphDB g;
    private String activeState = "";
    private long lastID;
    private ArrayList<Long> possibleWay = new ArrayList<>();
    private boolean validWay = false;

    /**
     * Create a new GraphBuildingHandler.
     *
     * @param g The graph to populate with the XML data.
     */
    public GraphBuildingHandler(GraphDB g) {
        this.g = g;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equals("node")) {
            long id = Long.parseLong(attributes.getValue("id"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            double lat = Double.parseDouble(attributes.getValue("lat"));
            g.addNode(id, lon, lat);
            lastID = id;
        } else if (qName.equals("way")) {
            activeState = "way";
            long id = Long.parseLong(attributes.getValue("id"));
            lastID = id;
        } else if (activeState.equals("way") && qName.equals("nd")) {
            long id = Long.parseLong(attributes.getValue("ref"));
            possibleWay.add(id);
        } else if (activeState.equals("way") && qName.equals("tag")) {
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("highway")) {
                if (ALLOWED_HIGHWAY_TYPES.contains(v)) {
                    validWay = true;
                } else {
                    validWay = false;
                }
            }
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {
            g.getNodes().get(lastID).addName(attributes.getValue("v"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            if (validWay) {
                for (int i = 0; i + 1 < possibleWay.size(); i++) {
                    g.connect(possibleWay.get(i), possibleWay.get(i + 1), lastID);
                }
            }
            validWay = false;
            possibleWay = new ArrayList<>();
        }
    }

}
