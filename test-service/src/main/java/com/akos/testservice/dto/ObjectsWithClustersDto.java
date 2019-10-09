package com.akos.testservice.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ObjectsWithClustersDto {

    private List<ClusterDto> clusters = new ArrayList<>();

    private List<GeoObjectDto> objects = new ArrayList<>();
}
