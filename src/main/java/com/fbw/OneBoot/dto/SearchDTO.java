package com.fbw.OneBoot.dto;

import lombok.Data;

@Data
public class SearchDTO {
    private String search;
    private Integer page;
    private Integer size;
}
