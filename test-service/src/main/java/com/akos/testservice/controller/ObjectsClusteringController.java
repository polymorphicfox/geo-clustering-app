package com.akos.testservice.controller;

import com.akos.testservice.dto.GeoObjectsClusteringParams;
import com.akos.testservice.dto.ObjectsWithClustersDto;
import com.akos.testservice.service.ObjectClusteringService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/clustered/objects")
public class ObjectsClusteringController {

    private ObjectClusteringService clusteringService;

    public ObjectsClusteringController(ObjectClusteringService clusteringService) {
        this.clusteringService = clusteringService;
    }

    @GetMapping
    public ObjectsWithClustersDto getObjectsClustered(@Valid GeoObjectsClusteringParams params) {
        return clusteringService.getObjectsWithClusters(params);
    }
}
