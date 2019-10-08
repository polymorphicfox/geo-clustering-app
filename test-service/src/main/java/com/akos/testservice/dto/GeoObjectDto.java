package com.akos.testservice.dto;

import lombok.Data;

@Data
public class GeoObjectDto {

    private String id;

    private Double latitude;

    private Double longitude;

    private String address;

    private String city;

    private String name;

    private String quadkey;
}
