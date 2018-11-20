package com.bk.dictionary.repository;

import com.bk.dictionary.model.Oxford.*;
import com.bk.dictionary.model.OxfordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Repository
public class CachedOxfordResponseRepository {
    @Value("${oxford.base-url}")
    private String apiEndpoint;
    @Value("${oxford.app-id}")
    private String appId;
    @Value("${oxford.app-key}")
    private String appKey;


    private final RestTemplate restTemplate;

    public CachedOxfordResponseRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "oxford", key = "#meaningResponse.cacheKey")
    public String findMeaningByCacheKey(OxfordResponse meaningResponse, String textInput) {
        String url = apiEndpoint + "/entries/en/" + textInput;
        System.out.println(url);

        try {

            WordApiResult oxfordWordResult = callWordApi(url);

            System.out.println(oxfordWordResult);

            WordResult wordResults = oxfordWordResult.getResults().stream().findFirst().orElse(null);
            WordLexicalEntry wordLexicalEntry = wordResults.getLexicalEntries().stream().findFirst().orElse(null);
            WordEntry entry = wordLexicalEntry.getEntries().stream().findFirst().orElse(null);
            WordSense sense = entry.getSenses().stream().findFirst().orElse(null);
            String definitions = sense.getDefinitions().stream().findFirst().orElse(null);

            if (!definitions.isEmpty() || definitions != null) {
                String lexicalCategory = wordLexicalEntry.getLexicalCategory();
                meaningResponse.setText(strFirstToUpper(textInput) + " (" + lexicalCategory + " ) :\n" + strFirstToUpper(definitions) + ".");
            }
            return meaningResponse.getText();

        } catch (Exception e) {
            System.out.println("e.findMeaningByCacheKey = " + e.getMessage());
            return null;
        }
    }

    @Cacheable(value = "oxford", key = "#synonymsResponse.cacheKey")
    public String findSynonymsByCacheKey(OxfordResponse synonymsResponse, String textInput) {

        String url = apiEndpoint + "/entries/en/" + textInput + "/synonyms";
        System.out.println("url = " + url);

        try {
            SynonymApiResult oxfordSynonymResult = callSynonymApi(url);

            SynonymResult synonymResult = oxfordSynonymResult.getResults().stream().findFirst().orElse(null);
            SynonymLexicalEntry synonymLexicalEntry = synonymResult.getLexicalEntries().stream().findFirst().orElse(null);
            SynonymEntry entry = synonymLexicalEntry.getEntries().stream().findFirst().orElse(null);
            SynonymSense sense = entry.getSenses().stream().findFirst().orElse(null);
            SynonymSubsenses subsenses = sense.getSubsenses().stream().findFirst().orElse(null);
            String synonyms = subsenses.getSynonyms().stream()
                    .limit(5)
                    .map(n -> strFirstToUpper(n.getText()))
                    .collect(Collectors.joining(", "));

            int lastDelimiter = synonyms.lastIndexOf(", ");
            String newSynonyms = synonyms.substring(0, lastDelimiter) + " and " + synonyms.substring((lastDelimiter + 1));

            return newSynonyms;
        } catch (Exception e) {
            System.out.println("e.findMeaningByCacheKey = " + e.getMessage());
            return null;
        }

    }


    private String strFirstToUpper(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private WordApiResult callWordApi(String url) {
        MultiValueMap<String, String> headers = setHeader();
        ResponseEntity<WordApiResult> response = null;
        try {
            System.out.println("Debug response = " + response);
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<Object>(headers),
                    WordApiResult.class
            );
        } catch (HttpClientErrorException e) {
            System.out.println("e.WordApiResult = " + e.getMessage());
            return null;
        }
        return response.getBody();
    }


    private MultiValueMap<String, String> setHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("app_id", appId);
        headers.add("app_key", appKey);
        return headers;
    }

    private SynonymApiResult callSynonymApi(String url) {
        MultiValueMap<String, String> headers = setHeader();
        ResponseEntity<SynonymApiResult> response = null;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<Object>(headers),
                    SynonymApiResult.class
            );
            System.out.println("response = " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            return null;
        }
        return response.getBody();
    }


}
