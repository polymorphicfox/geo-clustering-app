package com.akos.testservice.repository;

import com.akos.testservice.domain.GeoObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GeoObjectRepo extends PagingAndSortingRepository<GeoObject, String> , CustomGeoObjectRepo{

    Page<GeoObject> findAll(Pageable pageRequest);


}
