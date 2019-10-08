package com.akos.testservice.service;

import com.akos.testservice.domain.GeoObject;
import com.akos.testservice.dto.GeoObjectDto;
import com.akos.testservice.dto.PageableDto;
import com.akos.testservice.dto.filter.PageableFilter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GeoObjectDtoServiceImpl implements GeoObjectDtoService {

    private GeoObjectService geoObjectService;

    public GeoObjectDtoServiceImpl(GeoObjectService geoObjectService) {
        this.geoObjectService = geoObjectService;
    }

    @Override
    public PageableDto<GeoObject, GeoObjectDto> getObjects(PageableFilter filter) {

        Page<GeoObject> objectsPage = geoObjectService.getObjects(filter);
        return new PageableDto<>(objectsPage, this::toDto);
    }

    private GeoObjectDto toDto(GeoObject object) {
        GeoObjectDto dto = new GeoObjectDto();
        dto.setId(object.getId());
        dto.setAddress(object.getAddress());
        dto.setCity(object.getCity());
        dto.setName(object.getName());
        dto.setQuadkey(object.getQuadkey());
        dto.setLatitude(object.getLatitude());
        dto.setLongitude(object.getLongitude());
        return dto;
    }
}
