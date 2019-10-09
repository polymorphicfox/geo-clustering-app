package com.akos.testservice.util.geo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clustering utils based on the spherical form of the Mercator map projection (https://en.wikipedia.org/wiki/Mercator_projection)
 * Document that describes the projection, coordinate systems, and addressing scheme of the map tiles:
 * https://docs.microsoft.com/en-us/bingmaps/articles/bing-maps-tile-system?redirectedfrom=MSDN
 *
 */
public class QtreeTileUtils {

    private static final double EARTH_RADIUS = 6378137;
    /*
     * Since the Mercator projection goes to infinity at the poles, it doesn’t actually show the entire world.
     * Using a square aspect ratio for the map, the maximum latitude shown is approximately 85.05 degrees.
     */
    private static final double MIN_LATITUDE = -85.05112878;
    private static final double MAX_LATITUDE = 85.05112878;
    private static final double MIN_LONGITUDE = -180;
    private static final double MAX_LONGITUDE = 180;

    private static final int MAX_LEVEL_OF_DETAIL = 23;
    private static final int MIN_LEVEL_OF_DETAIL = 1;

    /**
     * Clips a number to the specified minimum and maximum values.
     *
     * @param n        the number to clip.
     * @param minValue minimum allowable value.
     * @param maxValue maximum allowable value.
     * @return the clipped value.
     */
    private static double clip(double n, double minValue, double maxValue) {
        return Math.min(Math.max(n, minValue), maxValue);
    }

    /**
     * Determines the map width and height (in pixels) at a specified level of detail.
     *
     * @param levelOfDetail level of detail, from 1 (lowest detail) to 23 (highest detail).
     * @return the map width and height in pixels.
     */
    public static long mapSize(int levelOfDetail) {
        return (long) 256 << levelOfDetail;
    }

    /**
     * Determines the ground resolution (in meters per pixel) at a specified latitude and level of detail.
     * The ground resolution indicates the distance on the ground that’s represented by a single pixel in the map.
     *
     * @param latitude      latitude (in degrees) at which to measure the ground resolution.
     * @param levelOfDetail level of detail, from 1 (lowest detail) to 23 (highest detail).
     * @return the ground resolution, in meters per pixel.
     */
    public static double groundResolution(double latitude, int levelOfDetail) {
        latitude = clip(latitude, MIN_LATITUDE, MAX_LATITUDE);
        return Math.cos(latitude * Math.PI / 180) * 2 * Math.PI * EARTH_RADIUS / mapSize(levelOfDetail);
    }

    /**
     * Converts a point from latitude/longitude WGS-84 coordinates (in degrees)
     * into pixel XY coordinates at a specified level of detail.
     *
     * @param geoPoint      latitude and longitude of the point, in degrees.
     * @param levelOfDetail level of detail, from 1 (lowest detail) to 23 (highest detail)
     * @return X and Y coordinates in pixels
     */
    public static PixelXYPoint latLongToPixelXY(LatLongPoint geoPoint, int levelOfDetail) {
        double latitude = clip(geoPoint.getLatitude(), MIN_LATITUDE, MAX_LONGITUDE);
        double longitude = clip(geoPoint.getLongitude(), MIN_LONGITUDE, MAX_LONGITUDE);

        double x = (longitude + 180) / 360;
        double sinLatitude = Math.sin(latitude * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

        long mapSize = mapSize(levelOfDetail);
        int pixelX = (int) clip(x * mapSize + 0.5, 0, mapSize - 1);
        int pixelY = (int) clip(y * mapSize + 0.5, 0, mapSize - 1);

        return new PixelXYPoint(pixelX, pixelY);
    }

    /**
     * Converts a pixel from pixel XY coordinates at a specified level of detail
     * into latitude/longitude WGS-84 coordinates (in degrees).
     *
     * @param pixelPoint    X Y coordinates of the point, in pixels.
     * @param levelOfDetail level of detail, from 1 (lowest detail) to 23 (highest detail)
     * @return latitude and longitude in degrees.
     */
    public static LatLongPoint pixelXYToLatLong(PixelXYPoint pixelPoint, int levelOfDetail) {

        double mapSize = mapSize(levelOfDetail);
        double x = (clip(pixelPoint.getX(), 0, mapSize - 1) / mapSize) - 0.5;
        double y = 0.5 - (clip(pixelPoint.getY(), 0, mapSize - 1) / mapSize);

        double latitude = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
        double longitude = 360 * x;

        return new LatLongPoint(latitude, longitude);
    }

    /**
     * Converts pixel XY coordinates into tile XY coordinates of the tile containing the specified pixel.
     *
     * @param pixelPoint X Y coordinates of the point, in pixels.
     * @return tile X Y coordinates.
     */
    public static TileXYPoint pixelXYToTileXY(PixelXYPoint pixelPoint) {
        int tileX = pixelPoint.getX() / 256;
        int tileY = pixelPoint.getY() / 256;
        return new TileXYPoint(tileX, tileY);
    }

    /**
     * Converts tile XY coordinates into pixel XY coordinates of the upper-left pixel of the specified tile.
     *
     * @param tilePoint tile X Y coordinates.
     * @return upper-left pixel X Y coordinates.
     */
    public static PixelXYPoint tileXYToPixelXY(TileXYPoint tilePoint) {
        int pixelX = tilePoint.getX() * 256;
        int pixelY = tilePoint.getY() * 256;

        return new PixelXYPoint(pixelX, pixelY);
    }

    /**
     * Converts tile XY coordinates into a QuadKey at a specified level of detail.
     *
     * @param tilePoint     tile X Y coordinates.
     * @param levelOfDetail level of detail, from 1 (lowest detail) to 23 (highest detail)
     * @return a string containing the QuadKey.
     */
    public static String tileXYToQuadKey(TileXYPoint tilePoint, int levelOfDetail) {
        StringBuilder quadKey = new StringBuilder();

        for (int i = levelOfDetail; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tilePoint.getX() & mask) != 0) {
                digit++;
            }
            if ((tilePoint.getY() & mask) != 0) {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
    }

    /**
     * Converts a QuadKey into tile XY coordinates.
     *
     * @param quadKey quadKey of the tile.
     * @return tile X Y coordinates with the level of detail.
     */
    public static TileXYPoint quadKeyToTileXY(String quadKey) {
        int tileX = 0;
        int tileY = 0;
        int levelOfDetail = quadKey.length();
        for (int i = levelOfDetail; i > 0; i--) {
            int mask = 1 << (i - 1);
            switch (quadKey.charAt(levelOfDetail - i)) {
                case '0':
                    break;

                case '1':
                    tileX |= mask;
                    break;

                case '2':
                    tileY |= mask;
                    break;

                case '3':
                    tileX |= mask;
                    tileY |= mask;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid QuadKey digit sequence.");
            }
        }
        return new TileXYPoint(tileX, tileY);
    }

    public static String latLongToQuadKey(LatLongPoint geoPoint, int levelOfDetail) {
        PixelXYPoint pixelPoint = latLongToPixelXY(geoPoint, levelOfDetail);
        TileXYPoint tilePoint = pixelXYToTileXY(pixelPoint);
        return tileXYToQuadKey(tilePoint, levelOfDetail);
    }

    public static String latLongToQuadKey(LatLongPoint geoPoint) {
        PixelXYPoint pixelPoint = latLongToPixelXY(geoPoint, MAX_LEVEL_OF_DETAIL);
        TileXYPoint tilePoint = pixelXYToTileXY(pixelPoint);
        return tileXYToQuadKey(tilePoint, MAX_LEVEL_OF_DETAIL);
    }

    public static LatLongPoint quadKeyToLatLong(String quadKey) {
        int levelOfDetail = quadKey.length();
        TileXYPoint tilePoint = quadKeyToTileXY(quadKey);
        PixelXYPoint pixelPoint = tileXYToPixelXY(tilePoint);
        return pixelXYToLatLong(pixelPoint, levelOfDetail);
    }

    public static List<String> getQuadkeysInBounds(LatLongPoint northWestPoint, LatLongPoint southEastPoint, int levelOfDetails) {
        TileXYPoint northWestTileXY = pixelXYToTileXY(latLongToPixelXY(northWestPoint, levelOfDetails));
        TileXYPoint southEastTileXY = pixelXYToTileXY(latLongToPixelXY(southEastPoint, levelOfDetails));

        List<String> quadkeys = new ArrayList<>();
        if (northWestTileXY.getX() <= southEastTileXY.getX()) {
            getQuadkeysForTilesInMapBounds(quadkeys, northWestTileXY, southEastTileXY, levelOfDetails);
        } else {
            getQuadkeysForTilesOutOfMapBounds(quadkeys, northWestTileXY, southEastTileXY, levelOfDetails);
        }
        return quadkeys;
    }

    //Если крайний западный и крайний восточный тайл не выходят за границы карты (Координата X западного тайла меньше координаты X восточного)
    private static void getQuadkeysForTilesInMapBounds(List<String> quadkeys, TileXYPoint northWestTileXY, TileXYPoint southEastTileXY, int levelOfDetails) {
        for (int i = northWestTileXY.getX(); i <= southEastTileXY.getX(); i++) {
            for (int j = northWestTileXY.getY(); j <= southEastTileXY.getY(); j++) {
                quadkeys.add(tileXYToQuadKey(new TileXYPoint(i, j), levelOfDetails));
            }
        }
    }

    //Если между крайним западным и крайним восточным тайлами не находится граница карты. (Координата X западного тайла больше координаты X восточного)
    private static void getQuadkeysForTilesOutOfMapBounds(List<String> quadkeys, TileXYPoint northWestTileXY, TileXYPoint southEastTileXY, int levelOfDetails) {
        for (int i = 0; i <= southEastTileXY.getX(); i++) {
            for (int j = northWestTileXY.getY(); j <= southEastTileXY.getY(); j++) {
                quadkeys.add(tileXYToQuadKey(new TileXYPoint(i, j), levelOfDetails));
            }
        }

        int maxEastTileX = pixelXYToTileXY(latLongToPixelXY(new LatLongPoint(0, MAX_LONGITUDE), levelOfDetails)).getX();

        for (int i = northWestTileXY.getX(); i <= maxEastTileX; i++) {
            for (int j = northWestTileXY.getY(); j <= southEastTileXY.getY(); j++) {
                quadkeys.add(tileXYToQuadKey(new TileXYPoint(i, j), levelOfDetails));
            }
        }
    }
}
