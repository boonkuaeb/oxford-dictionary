package com.bk.dictionary.model.Oxford;

import lombok.Data;

import java.util.List;

@Data
public class SynonymEntry {
    List<SynonymSense> senses;
}
