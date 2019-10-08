package com.akos.testservice.service;

import com.akos.testservice.domain.GeoObject;
import com.akos.testservice.dto.filter.PageableFilter;
import org.springframework.data.domain.Page;

public interface GeoObjectService {

    Page<GeoObject> getObjects(PageableFilter filter);
}
