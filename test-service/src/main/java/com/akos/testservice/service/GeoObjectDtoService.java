package com.akos.testservice.service;

import com.akos.testservice.domain.GeoObject;
import com.akos.testservice.dto.GeoObjectDto;
import com.akos.testservice.dto.PageableDto;
import com.akos.testservice.dto.filter.PageableFilter;

public interface GeoObjectDtoService {

    PageableDto<GeoObject, GeoObjectDto> getObjects(PageableFilter filter);
}
