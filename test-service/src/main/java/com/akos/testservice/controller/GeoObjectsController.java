package com.akos.testservice.controller;

import com.akos.testservice.domain.GeoObject;
import com.akos.testservice.dto.GeoObjectDto;
import com.akos.testservice.dto.PageableDto;
import com.akos.testservice.dto.filter.PageableFilter;
import com.akos.testservice.service.GeoObjectDtoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/objects")
public class GeoObjectsController {

    private GeoObjectDtoService geoObjectDtoService;

    public GeoObjectsController(GeoObjectDtoService geoObjectDtoService) {
        this.geoObjectDtoService = geoObjectDtoService;
    }

    @GetMapping
    @CrossOrigin
    public PageableDto<GeoObject, GeoObjectDto> geoObjects(@Valid PageableFilter filter) {
        return geoObjectDtoService.getObjects(filter);
    }
}
