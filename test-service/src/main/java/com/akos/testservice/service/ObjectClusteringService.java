package com.akos.testservice.service;

import com.akos.testservice.dto.GeoObjectsClusteringParams;
import com.akos.testservice.dto.ObjectsWithClustersDto;

public interface ObjectClusteringService {

    ObjectsWithClustersDto getObjectsWithClusters(GeoObjectsClusteringParams params);
}
