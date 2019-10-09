package com.akos.testservice.dto;

import com.akos.testservice.util.geo.LatLongPoint;
import lombok.Data;

@Data
public class GeoObjectDto {

    private String id;

    private LatLongPoint coordinates;

    private String address;

    private String city;

    private String name;

    private String quadkey;
}
