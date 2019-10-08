package com.akos.testservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingDto {
    private long size;
    private long totalElements;
    private long totalPages;
    private long number;
}
