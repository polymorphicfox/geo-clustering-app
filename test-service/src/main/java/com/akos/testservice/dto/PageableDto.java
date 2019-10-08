package com.akos.testservice.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PageableDto<Entity, Dto> {
    private List<Dto> data;
    private PagingDto paging;

    public PageableDto(Page<Entity> page, Function<Entity, Dto> function) {
        this.data = page.getContent().stream()
                .map(function)
                .collect(Collectors.toList());
        this.paging = new PagingDto(page.getSize(), page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
}
