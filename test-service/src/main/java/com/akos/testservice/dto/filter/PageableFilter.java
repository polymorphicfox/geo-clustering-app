package com.akos.testservice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableFilter {

    private int page = 0;
    int pageSize = 20;
}
