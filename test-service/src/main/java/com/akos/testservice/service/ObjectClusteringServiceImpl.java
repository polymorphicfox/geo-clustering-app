package com.akos.testservice.service;

import com.akos.testservice.dto.ClusterDto;
import com.akos.testservice.dto.GeoObjectDto;
import com.akos.testservice.dto.GeoObjectsClusteringParams;
import com.akos.testservice.dto.ObjectsWithClustersDto;
import com.akos.testservice.repository.GeoObjectRepo;
import com.akos.testservice.util.clustering.ClusteringUtils;
import com.akos.testservice.util.geo.LatLongPoint;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectClusteringServiceImpl implements ObjectClusteringService {

    private static final int MIN_CLUSTER_SIZE = 2;

    private static final int MAX_OBJECTS_IN_CLUSTER = 200;

    private static final int ADDITIONAL_PRECISION = 1;

    private GeoObjectRepo objectRepo;

    public ObjectClusteringServiceImpl(GeoObjectRepo objectRepo) {
        this.objectRepo = objectRepo;
    }

    @Override
    public ObjectsWithClustersDto getObjectsWithClusters(GeoObjectsClusteringParams params) {

        int levelOfDetail = ClusteringUtils.resolveLevelOfDetail(params.getZoomLevel());

        String quadkeysQueryString = ClusteringUtils.getQuadkeysQueryString(params.getLatNorthWest(), params.getLonNorthWest(),
                params.getLatSouthEast(), params.getLonSouthEast(), levelOfDetail);

        List<Object[]> objects = objectRepo.findAllObjectsInTiles(quadkeysQueryString);

        Map<String, List<GeoObjectDto>> clusters = new HashMap<>();

        fillClusters(clusters, objects, levelOfDetail);

        return mapObjectsWithClusters(clusters);
    }

    private ObjectsWithClustersDto mapObjectsWithClusters(Map<String, List<GeoObjectDto>> clusters) {
        ObjectsWithClustersDto objectsWithClusters = new ObjectsWithClustersDto();

        clusters.values().forEach(clusteredObjects -> {
            int clusterSize = clusteredObjects.size();
            if (clusterSize < MIN_CLUSTER_SIZE) {
                objectsWithClusters.getObjects().addAll(clusteredObjects);
            } else {
                ClusterDto cluster = getClusterDto(clusteredObjects, clusterSize);
                objectsWithClusters.getClusters().add(cluster);
            }
        });
        return objectsWithClusters;
    }

    private ClusterDto getClusterDto(List<GeoObjectDto> clusteredObjects, int clusterSize) {
        ClusterDto cluster = new ClusterDto();
        if (clusterSize <= MAX_OBJECTS_IN_CLUSTER) {
            List<String> shopIdList = clusteredObjects.stream()
                    .map(GeoObjectDto::getId)
                    .collect(Collectors.toList());
            cluster.setObjects(shopIdList);
        }
        cluster.setCoordinates(ClusteringUtils.findGeographicMidpoint(clusteredObjects));
        cluster.setObjectsCount(clusterSize);
        return cluster;
    }

    private void fillClusters(Map<String, List<GeoObjectDto>> clusters, List<Object[]> shops, int levelOfDetail) {
        shops.forEach(shop -> {
            String shopQuadkey = (String) shop[3];

            GeoObjectDto apiShop = mapToDto(shop);

            String clusterKey = shopQuadkey.substring(0, levelOfDetail + ADDITIONAL_PRECISION);
            if (clusters.containsKey(clusterKey)) {
                clusters.get(clusterKey).add(apiShop);
            } else {
                clusters.put(clusterKey, new ArrayList<>(Collections.singletonList(apiShop)));
            }
        });
    }

    private GeoObjectDto mapToDto(Object[] objects) {
        GeoObjectDto dto = new GeoObjectDto();
        dto.setId((String) objects[0]);
        LatLongPoint coordinates = new LatLongPoint();
        coordinates.setLatitude((Double) objects[1]);
        coordinates.setLongitude((Double) objects[2]);
        dto.setCoordinates(coordinates);
        dto.setName((String) objects[4]);
        return dto;
    }
}
