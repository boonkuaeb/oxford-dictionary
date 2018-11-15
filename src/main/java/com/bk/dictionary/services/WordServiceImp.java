package com.bk.dictionary.services;


import com.bk.dictionary.model.Synonyms;
import com.bk.dictionary.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WordServiceImp implements WordService {

    @Value("${oxford.base-url}")
    private String apiEndpoint;

    @Value("${oxford.app-id}")
    private String appId;

    @Value("${oxford.app-key}")
    private String appKey;


    private final RestTemplate restTemplate;

    @Autowired
    public WordServiceImp(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Word getMeaning(String text) {

        text = cleanUpText(text);
        if (text.isEmpty()) {
            return new Word(false);
        }
        HttpHeaders headers = initApiCall();
        String url = apiEndpoint + "/entries/en/" + text;
        try {
            ResponseEntity<String> response = callOxfordApi(headers, url);
            String[] definitionAndExample = findFirstDefinitionNode(response);
            String definitionsFinal = getDefinition(definitionAndExample[0]);

            Word word = new Word(false);
            if (definitionsFinal.length() > 0) {
                word.setStatus(true);
                word.setMean("\"" + strFirstToUpper(text) + "\" :\n" + strFirstToUpper(definitionsFinal));
            }
            return word;
        } catch (Exception e) {
            e.getMessage();
            System.out.println("e.getMessage() = " + e.getMessage());
            System.out.println("e.getStackTrace() = " + e.getStackTrace());
        }
        return new Word(false);
    }

    public Synonyms getSynonyms(String text) {
        text = cleanUpText(text);
        if (text.isEmpty()) {
            return new Synonyms(false);
        }

        HttpHeaders headers = initApiCall();
        String url = apiEndpoint + "/entries/en/" + text + "/synonyms";
        try {
            ResponseEntity<String> responseSynonyms = callOxfordApi(headers, url);

            String responseBody = responseSynonyms.getBody();

            String[] topFirstSynonyms = extractSynonyms(responseBody);

            Synonyms synonyms = new Synonyms(false);
            if (topFirstSynonyms.length > 0) {
                synonyms.setStatus(true);
                synonyms.setTextSynonyms("Similar Words:\n" + String.join(", ", topFirstSynonyms));
            }
            return synonyms;

        } catch (Exception e) {
            e.getMessage();
            System.out.println("e.getMessage() = " + e.getMessage());
            System.out.println("e.getStackTrace() = " + e.getStackTrace());
        }

        return new Synonyms(false);
    }

    public String[] extractSynonyms(String textPlayload) {
        String[] synonyms = new String[5];
        String[] synonymsArrayList = textPlayload.split("synonyms\": \\[", 6);

        int synonymsIndex = 0;
        for (int i = 1; i < synonymsArrayList.length; i++) {
            String[] synonymsTextList = synonymsArrayList[i].split("\"text\": ");

            for (int j = 1; j < synonymsTextList.length; j++) {
                String[] stringWithEndTag = synonymsTextList[j].split("\",", 2);
                for (int k = 0; k < stringWithEndTag.length; k++) {
                    String[] stringRemoveNewLine = stringWithEndTag[k].split("\n", 2);
                    String synonymsText = stringRemoveNewLine[0].replace("\"", "").trim();
                    if (!synonymsText.trim().isEmpty()) {
                        synonyms[synonymsIndex] = synonymsText;
                        synonymsIndex += 1;
                    }
                    if (synonymsIndex >= 5) {
                        break;
                    }
                }
                if (synonymsIndex >= 5) {
                    break;
                }
            }
            if (synonymsIndex >= 5) {
                break;
            }
        }


        return synonyms;
    }


    private String[] findFirstDefinitionNode(ResponseEntity<String> response) {
        String textBody = response.getBody();
        String[] definitions = textBody.split("\"definitions\": \\[", 3);
        if (definitions.length > 1) {
            return definitions[1].split("],", 2);
        }
        String[] crossReferenceMarkers = textBody.split("\"crossReferenceMarkers\": \\[");
        if (crossReferenceMarkers.length > 1) {
            return crossReferenceMarkers[1].split("],", 2);

        }
        String[] cannotFindMeaning = new String[1];
        cannotFindMeaning[0] = "Cannot find the meaning for this time.";
        return cannotFindMeaning;

    }

    private String getDefinition(String definitionText) {
        String[] definitionsWithQuote = definitionText.split("\"");
        return definitionsWithQuote[1].trim();
    }

    private String strFirstToUpper(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);

    }

    private String cleanUpText(String text) {
        String[] textArr = text.trim().split(" ");
        text = textArr[0].replaceAll("[^a-zA-Z0-9]", "");
        return text;
    }

    private ResponseEntity<String> callOxfordApi(HttpHeaders headers, String url) {
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);
        System.out.println("response = " + response);
        return response;
    }

    private HttpHeaders initApiCall() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("app_id", appId);
        headers.set("app_key", appKey);
        return headers;
    }

}
