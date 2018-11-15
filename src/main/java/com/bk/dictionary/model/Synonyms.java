package com.bk.dictionary.model;

import lombok.Data;

@Data
public class Synonyms {
    private Boolean status;
    private String textSynonyms;

    public Synonyms(Boolean status) {
        this.status = status;
    }
}
