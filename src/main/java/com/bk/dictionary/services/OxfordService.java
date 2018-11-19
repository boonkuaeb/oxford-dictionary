package com.bk.dictionary.services;


import com.bk.dictionary.model.Oxford.*;
import com.bk.dictionary.model.OxfordResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Service
public class OxfordService {

    private final RestTemplate restTemplate;
    @Value("${oxford.base-url}")
    private String apiEndpoint;
    @Value("${oxford.app-id}")
    private String appId;
    @Value("${oxford.app-key}")
    private String appKey;

    @Autowired
    public OxfordService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OxfordResponse getMeaning(String text) {
        OxfordResponse meaningResponse = new OxfordResponse(false);
        text = cleanUpText(text);
        if (text.isEmpty()) {
            return meaningResponse;
        }

        try {
            String url = apiEndpoint + "/entries/en/" + text;
            System.out.println(url);

            WordApiResult oxfordWordResult = callWordApi(url);

            System.out.println(oxfordWordResult);

            WordResult wordResults = oxfordWordResult.getResults().stream().findFirst().orElse(null);
            WordLexicalEntry wordLexicalEntry = wordResults.getLexicalEntries().stream().findFirst().orElse(null);
            WordEntry entry = wordLexicalEntry.getEntries().stream().findFirst().orElse(null);
            WordSense sense = entry.getSenses().stream().findFirst().orElse(null);
            String definitions = sense.getDefinitions().stream().findFirst().orElse(null);

            if (!definitions.isEmpty() || definitions != null) {
                meaningResponse.setStatus(true);
                String lexicalCategory = wordLexicalEntry.getLexicalCategory();
                meaningResponse.setText(strFirstToUpper(text) + " (" + lexicalCategory + " ) :\n" + strFirstToUpper(definitions) + ".");
            }

            return meaningResponse;
        } catch (Exception e) {
            System.out.println("e.OxfordResponse() = " + e.getMessage());
            return meaningResponse;
        }
    }

    public OxfordResponse getSynonyms(String text) {
        OxfordResponse synonymsResponse = new OxfordResponse(false);
        text = cleanUpText(text);
        if (text.isEmpty()) {
            return new OxfordResponse(false);
        }
        try {
            String url = apiEndpoint + "/entries/en/" + text + "/synonyms";
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

            System.out.println("newSynonyms = " + newSynonyms);
            synonymsResponse.setStatus(true);
            synonymsResponse.setText("Similar Words :\n" + strFirstToUpper(newSynonyms) + ".");


            return synonymsResponse;

        } catch (Exception e) {
            e.getMessage();
            System.out.println("getSynonyms.getMessage() = " + e.getMessage());
        }
        return synonymsResponse;
    }


    private String strFirstToUpper(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);

    }

    private String cleanUpText(String text) {
        String[] textArray = text.trim().split(" ");
        text = textArray[0].replaceAll("[^a-zA-Z0-9]", "");
        return text;
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
            System.out.println("debug response = " + response);
        } catch (HttpClientErrorException e) {
            System.out.println("e.WordApiResult = " + e.getMessage());
            return null;
        }
        return response.getBody();
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


    private MultiValueMap<String, String> setHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("app_id", appId);
        headers.add("app_key", appKey);
        return headers;
    }

}
