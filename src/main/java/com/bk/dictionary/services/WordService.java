package com.bk.dictionary.services;


import com.bk.dictionary.model.Synonyms;
import com.bk.dictionary.model.Word;

public interface WordService {
    Word getMeaning(String text);
    Synonyms getSynonyms(String text);
}
