package com.bk.dictionary.services;


import com.bk.dictionary.model.OxfordResponse;
import com.bk.dictionary.repository.CachedOxfordResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OxfordService {

    private PushMessageService pushMessageService;
    private CachedOxfordResponseRepository cachedOxfordResponseRepositoryImp;

    @Autowired
    public OxfordService(PushMessageService pushMessageService, CachedOxfordResponseRepository cachedOxfordResponseRepositoryImp) {
        this.pushMessageService = pushMessageService;
        this.cachedOxfordResponseRepositoryImp = cachedOxfordResponseRepositoryImp;
    }

    @Async
    public void translate(String userId, String text) {
        text = cleanUpText(text);
        if (!text.isEmpty()) {
            try {
                OxfordResponse meaningResponse = new OxfordResponse();
                meaningResponse.setCacheKey("m_" + text);
                String message = cachedOxfordResponseRepositoryImp.findMeaningByCacheKey(meaningResponse, text);

                pushMessageService.pushMessage(userId, message);
                getSynonyms(userId, text);

            } catch (Exception e) {
                System.out.println("e.OxfordResponse() = " + e.getMessage());
                OxfordResponse meaningResponse = new OxfordResponse();
                meaningResponse.setText("Cannot find the meaning for this time.");
                pushMessageService.pushMessage(userId, meaningResponse.getText());
            }
        }

    }

    public void getSynonyms(String userId, String text) {
        try {

            OxfordResponse synonymsResponse = new OxfordResponse();
            synonymsResponse.setCacheKey("s_" + text);

            String newSynonyms = cachedOxfordResponseRepositoryImp.findSynonymsByCacheKey(synonymsResponse, text);

            System.out.println("newSynonyms = " + newSynonyms);
            synonymsResponse.setText("Similar Words :\n" + strFirstToUpper(newSynonyms) + ".");
            pushMessageService.pushMessage(userId, synonymsResponse.getText());
        } catch (Exception e) {
            e.getMessage();
            System.out.println("getSynonyms.getMessage() = " + e.getMessage());
        }
    }

    private String strFirstToUpper(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);

    }

    private String cleanUpText(String text) {
        String[] textArray = text.trim().split(" ");
        text = textArray[0].replaceAll("[^a-zA-Z]", "");
        return text;
    }
}
