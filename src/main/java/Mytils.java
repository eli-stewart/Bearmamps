public class Mytils {

    public static int depthCalculator(double lonDPP) {
        int depth = 0;

        while (depth < 7) {
            double lonDPPImage = lonDPPImage(depth);
            if (lonDPPImage <= lonDPP) {
                break;
            }
            depth++;
        }
        return depth;
    }

    public static double lonDPPImage(int depth) {
        return MapServer.ROOT_LON_DELTA / (Math.pow(2, depth) * MapServer.TILE_SIZE);
    }

    public static double latDPPImage(int depth) {
        return MapServer.ROOT_LAT_DELTA / (Math.pow(2, depth) * MapServer.TILE_SIZE);
    }

    /* Returns an ArrayList of [x1, y1, x2, y2] */
    public static int[] cornerRasterFinder(RasterRequestParams params, int depth) {
        //double xMult = MapServer.TILE_SIZE * lonDPPImage(depth);
        //double yMult = MapServer.TILE_SIZE * latDPPImage(depth);

        int[] result = new int[4];

        int k = (int) (Math.pow(2, depth) - 1);

        // x1
        for (int x1 = 0; x1 <= k; x1++) {
            result[0] = x1;
            if (cornerToLon(x1 + 1, depth) >= params.ullon) {
                break;
            }
        }
        //x2
        for (int x2 = result[0]; x2 <= k; x2++) {
            result[2] = x2;
            if (cornerToLon(x2 + 1, depth) >= params.lrlon) {
                break;
            }
        }

        //y1
        for (int y1 = k; y1 >= 0; y1--) {
            result[1] = y1;
            if (cornerToLat(y1 - 1, depth) >= params.ullat) {
                break;
            }
        }

        //y2
        for (int y2 = k; y2 >= 0; y2--) {
            result[3] = y2;
            if (cornerToLat(y2 - 1, depth) >= params.lrlat) {
                break;
            }
        }
        return result;
    }

    public static String[][] imageToString(int[] rasterCorners, int depth) {
        int x1 = rasterCorners[0];
        int y1 = rasterCorners[1];
        int x2 = rasterCorners[2];
        int y2 = rasterCorners[3];

        String[][] result = new String[y2 - y1 + 1][x2 - x1 + 1];
        int xi = 0;
        int yi = 0;
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                result[yi][xi] = "d" + depth + "_x" + x + "_y" + y + ".png";
                yi++;
            }
            xi++;
            yi = 0;
        }
        return result;
    }

    /* Returns longitude of the left side of the square */
    public static Double cornerToLon(int i, int depth) {
        double coord = i * MapServer.TILE_SIZE * lonDPPImage(depth) + MapServer.ROOT_ULLON;
        return coord;
    }

    /* Returns the latitude of the bottom side of the square */
    public static Double cornerToLat(int i, int depth) {
        double coord = MapServer.ROOT_ULLAT - (i + 1) * MapServer.TILE_SIZE * latDPPImage(depth);
        return coord;
    }

}
