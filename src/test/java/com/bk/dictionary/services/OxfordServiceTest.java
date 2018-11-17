package com.bk.dictionary.services;

import com.bk.dictionary.model.Oxford.*;
import com.bk.dictionary.model.OxfordResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OxfordServiceTest {

    @Mock
    RestTemplate restTemplate;

    private OxfordService oxfordService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oxfordService = new OxfordService(restTemplate);
    }

    @Test
    public void getMeaning_Fail_When_InputBlank() {

        OxfordResponse actual = oxfordService.getMeaning("");
        assertEquals(false, actual.getStatus());
    }

    @Test
    public void getMeaning_Fail_When_ApiReturn404() {
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
        OxfordResponse actual = oxfordService.getMeaning("information");
        assertEquals(oxfordResponseMock.getStatus(), actual.getStatus());
        assertEquals(oxfordResponseMock.getText(), actual.getText());

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


        // Then
        OxfordResponse actual = oxfordService.getMeaning("information");
        assertEquals(oxfordResponseMock.getStatus(), actual.getStatus());

        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );

    }


    @Test
    public void getSynonyms_Fail_When_InputBlank() {

        OxfordResponse actual = oxfordService.getSynonyms("");
        assertEquals(false, actual.getStatus());
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
        OxfordResponse actual = oxfordService.getSynonyms("information");
        assertEquals(oxfordResponseMock.getStatus(), actual.getStatus());

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


        // Then
        OxfordResponse actual = oxfordService.getSynonyms("line");
        assertEquals(oxfordResponseMock.getStatus(), actual.getStatus());

        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );

    }

}