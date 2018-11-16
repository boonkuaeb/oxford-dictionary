package com.bk.dictionary.model;

import lombok.Data;

@Data
public class OxfordResponse {

    private Boolean status;
    private String text;

    public OxfordResponse(Boolean status) {
        this.status = status;
    }
}
