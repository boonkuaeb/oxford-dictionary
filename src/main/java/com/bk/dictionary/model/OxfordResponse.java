package com.bk.dictionary.model;

import lombok.Data;

@Data
public class OxfordResponse {

    private Boolean status;
    private String text;
    private String cacheKey;

}
