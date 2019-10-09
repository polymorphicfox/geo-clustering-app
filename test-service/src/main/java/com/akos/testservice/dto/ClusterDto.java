package com.akos.testservice.dto;

import com.akos.testservice.util.geo.LatLongPoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClusterDto {

    private LatLongPoint coordinates;

    private List<String> objects = new ArrayList<>();

    @JsonProperty("objects_count")
    private Integer objectsCount;
}
