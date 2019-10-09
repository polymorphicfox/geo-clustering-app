package com.akos.testservice.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GeoObjectsClusteringParams {

    @NotNull
    private Double latNorthWest;

    @NotNull
    private Double lonNorthWest;

    @NotNull
    private Double latSouthEast;

    @NotNull
    private Double lonSouthEast;

    @NotNull
    private Integer zoomLevel;
}
