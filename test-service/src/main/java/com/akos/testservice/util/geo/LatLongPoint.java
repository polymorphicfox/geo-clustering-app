package com.akos.testservice.util.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatLongPoint {
    private double latitude;
    private double longitude;
}