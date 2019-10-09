package com.akos.testservice.util.clustering;

import com.akos.testservice.dto.GeoObjectDto;
import com.akos.testservice.util.geo.LatLongPoint;
import com.akos.testservice.util.geo.QtreeTileUtils;

import java.util.List;

public class ClusteringUtils {

    public static String getQuadkeysQueryString(Double lat1, Double lon1, Double lat2, Double lon2, int levelOfDetail){
        List<String> quadkeys = QtreeTileUtils.getQuadkeysInBounds(new LatLongPoint(lat1, lon1), new LatLongPoint(lat2, lon2), levelOfDetail);
        return String.join("%,", quadkeys) + "%";
    }

    public static int resolveLevelOfDetail(int zoomLevel) {
        int levelOfDetail = zoomLevel;

        if (zoomLevel <= 3)
            levelOfDetail = 3;

        if (zoomLevel > 3 && zoomLevel <= 6)
            levelOfDetail = 4;

        if (zoomLevel == 6 || zoomLevel == 7)
            levelOfDetail = 6;

        if (zoomLevel == 8 || zoomLevel == 9)
            levelOfDetail = 8;

        if (zoomLevel == 13 || zoomLevel == 14)
            levelOfDetail = 13;

        if (zoomLevel>22)
            levelOfDetail=22;

        return levelOfDetail;
    }

    public static LatLongPoint findGeographicMidpoint(List<GeoObjectDto> objects) {
        double x = 0;
        double y = 0;
        double z = 0;

        for (GeoObjectDto object : objects) {
            double latRad = Math.toRadians(object.getCoordinates().getLatitude());
            double lonRad = Math.toRadians(object.getCoordinates().getLongitude());

            x = x + Math.cos(latRad) * Math.cos(lonRad);
            y = y + Math.cos(latRad) * Math.sin(lonRad);
            z = z + Math.sin(latRad);
        }

        x = x / objects.size();
        y = y / objects.size();
        z = z / objects.size();

        double lon = Math.atan2(y, x);
        double hyp = Math.sqrt(x * x + y * y);
        double lat = Math.atan2(z, hyp);

        return new LatLongPoint(Math.toDegrees(lat), Math.toDegrees(lon));
    }
}
