/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /**
     * The max image depth level.
     */
    public static final int MAX_DEPTH = 7;

    /**
     * Takes a user query and finds the grid of images that best matches the query. These images
     * will be combined into one big image (rastered) by the front end. The grid of images must obey
     * the following properties, where image in the grid is referred to as a "tile".
     * <ul>
     * <li>The tiles collected must cover the most longitudinal distance per pixel (LonDPP)
     * possible, while still covering less than or equal to the amount of longitudinal distance
     * per pixel in the query box for the user viewport size.</li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the above
     * condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params The RasterRequestParams containing coordinates of the query box and the browser
     *               viewport width and height.
     * @return A valid RasterResultParams containing the computed results.
     */
    public RasterResultParams getMapRaster(RasterRequestParams params) {

        // Corner Cases
        if (params.ullon > MapServer.ROOT_LRLON || params.ullat < MapServer.ROOT_LRLAT
                || params.lrlat > MapServer.ROOT_ULLAT || params.lrlon < MapServer.ROOT_ULLON) {
            return RasterResultParams.queryFailed();
        }
        if (params.ullon > params.lrlon || params.ullat < params.lrlat) {
            return RasterResultParams.queryFailed();
        }

        RasterResultParams.Builder builder = new RasterResultParams.Builder();
        double lonDPP = lonDPP(params.lrlon, params.ullon, params.w);
        int depth = (Mytils.depthCalculator(lonDPP));
        builder.setDepth(Mytils.depthCalculator(lonDPP));

        int[] corners = Mytils.cornerRasterFinder(params, depth);
        String[][] renderGrid = Mytils.imageToString(corners, depth);

        builder.setRenderGrid(renderGrid);

        builder.setRasterLrLat(Mytils.cornerToLat(corners[3], depth));
        builder.setRasterLrLon(Mytils.cornerToLon(corners[2] + 1, depth));
        builder.setRasterUlLat(Mytils.cornerToLat(corners[1] - 1, depth));
        builder.setRasterUlLon(Mytils.cornerToLon(corners[0], depth));

        builder.setQuerySuccess(true);

        return builder.create();
    }

    /**
     * Calculates the lonDPP of an image or query box
     *
     * @param lrlon Lower right longitudinal value of the image or query box
     * @param ullon Upper left longitudinal value of the image or query box
     * @param width Width of the query box or image
     * @return lonDPP
     */
    private double lonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }
}
