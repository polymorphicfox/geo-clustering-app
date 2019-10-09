package com.akos.testservice.repository;

import java.util.List;

public interface CustomGeoObjectRepo {

    List<Object[]> findAllObjectsInTiles(String quadkeys);
}
