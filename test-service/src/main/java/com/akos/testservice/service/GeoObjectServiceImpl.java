package com.akos.testservice.service;

import com.akos.testservice.domain.GeoObject;
import com.akos.testservice.dto.filter.PageableFilter;
import com.akos.testservice.repository.GeoObjectRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GeoObjectServiceImpl implements GeoObjectService {

    private GeoObjectRepo geoObjectRepo;

    public GeoObjectServiceImpl(GeoObjectRepo geoObjectRepo) {
        this.geoObjectRepo = geoObjectRepo;
    }

    public Page<GeoObject> getObjects(PageableFilter filter) {
        return geoObjectRepo.findAll(PageRequest.of(filter.getPage(), filter.getPageSize()));
    }
}
