package com.bk.dictionary.model;

import lombok.Data;

@Data
public class Word {

    private Boolean status;
    private String mean;

    public Word(Boolean status) {
        this.status = status;
    }
}
