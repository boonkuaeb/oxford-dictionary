package com.bk.dictionary.services;

import com.bk.dictionary.model.Oxford.*;
import com.bk.dictionary.model.OxfordResponse;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test") // Like this
public class OxfordServiceTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    PushMessageService pushMessageService;

    @InjectMocks
    private OxfordService oxfordService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oxfordService = new OxfordService(restTemplate, pushMessageService);

    }

    @Test
    public void getMeaning_Fail_When_InputBlank() {

        try {
            oxfordService.getMeaning("TESt", "");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof Exception);

        }
    }

    @Test
    public void getMeaning_Fail_When_ApiReturn404() {
        // Given

        // When
        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<WordApiResult>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));


        // Then
        try {
            oxfordService.getMeaning("userid", "line");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof Exception);
        }
        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );
    }


    @Test
    public void getMeaning_Success_When_ApiReturn() {
        // Given
        OxfordResponse oxfordResponseMock = new OxfordResponse(true);
        oxfordResponseMock.setText("Information");

        List<String> definitionList = Arrays.asList("Information", "sup2", "sup3");

        WordSense wordSense = new WordSense();
        wordSense.setDefinitions(definitionList);
        List<WordSense> senseList = Arrays.asList(wordSense, wordSense);

        WordEntry wordEntry = new WordEntry();
        wordEntry.setSenses(senseList);
        List<WordEntry> entryList = Arrays.asList(wordEntry, wordEntry);

        WordLexicalEntry lexicalEntry = new WordLexicalEntry();
        lexicalEntry.setEntries(entryList);
        List<WordLexicalEntry> lexicalEntryList = Arrays.asList(lexicalEntry, lexicalEntry);

        WordResult result = new WordResult();
        result.setLexicalEntries(lexicalEntryList);
        List<WordResult> resultList = Arrays.asList(result, result);

        WordApiResult apiResult = new WordApiResult();
        apiResult.setResults(resultList);

        ResponseEntity<WordApiResult> myEntity = new ResponseEntity<WordApiResult>(apiResult, HttpStatus.ACCEPTED);



        // When
        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<WordApiResult>>any())).
                thenReturn(myEntity);

        when(pushMessageService.pushMessage(
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn("message");

        // Then
        oxfordService.getMeaning("userid", "message");

        // Verify
        verify(restTemplate, times(2)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );

        verify(pushMessageService,times(1)).pushMessage(anyString(),anyString());

    }



    @Test
    public void getSynonyms_Fail_When_ApiReturn404() {
        // Given
        OxfordResponse oxfordResponseMock = new OxfordResponse(false);

        // When
        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<WordApiResult>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));


        // Then
        oxfordService.getSynonyms("userid","line");

        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );

    }


    @Test
    public void getSynonyms_Success_WhenApiReturn() {
        // Given
        OxfordResponse oxfordResponseMock = new OxfordResponse(true);
        oxfordResponseMock.setText("line");

        Synonym synonym1 = new Synonym();
        synonym1.setText("underline");

        Synonym synonym2 = new Synonym();
        synonym2.setText("underscore");

        Synonym synonym3 = new Synonym();
        synonym3.setText("stroke");

        Synonym synonym4 = new Synonym();
        synonym4.setText("slash");

        Synonym synonym5 = new Synonym();
        synonym5.setText("virgule");


        List<Synonym> synonymList = Arrays.asList(synonym1, synonym2, synonym3, synonym4, synonym5);

        SynonymSubsenses subsenses = new SynonymSubsenses();
        subsenses.setSynonyms(synonymList);
        List<SynonymSubsenses> subsensesList = Arrays.asList(subsenses,subsenses);

        SynonymSense synonymSense = new SynonymSense();
        synonymSense.setSubsenses(subsensesList);
        List<SynonymSense> senseList = Arrays.asList(synonymSense, synonymSense);

        SynonymEntry entry = new SynonymEntry();
        entry.setSenses(senseList);
        List<SynonymEntry> entryList = Arrays.asList(entry, entry);

        SynonymLexicalEntry lexicalEntry = new SynonymLexicalEntry();
        lexicalEntry.setEntries(entryList);
        List<SynonymLexicalEntry> lexicalEntryList = Arrays.asList(lexicalEntry, lexicalEntry);

        SynonymResult result = new SynonymResult();
        result.setLexicalEntries(lexicalEntryList);
        List<SynonymResult> resultList = Arrays.asList(result, result);

        SynonymApiResult apiResult = new SynonymApiResult();
        apiResult.setResults(resultList);

        ResponseEntity<SynonymApiResult> myEntity = new ResponseEntity<SynonymApiResult>(apiResult, HttpStatus.ACCEPTED);

        // When
        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<SynonymApiResult>>any())).
                thenReturn(myEntity);
        when(pushMessageService.pushMessage(
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn("message");


        // Then
        oxfordService.getSynonyms("userid","line");

        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );

        verify(pushMessageService,times(1)).pushMessage(anyString(),anyString());


    }

}