package com.bk.dictionary.repository;

import com.bk.dictionary.model.Oxford.*;
import com.bk.dictionary.model.OxfordResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CachedOxfordResponseRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CachedOxfordResponseRepository cachedOxfordResponseRepositoryImp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        cachedOxfordResponseRepositoryImp = new CachedOxfordResponseRepository(restTemplate);

    }

    @Test
    public void findMeaningByCacheKey_Fail_When_API404() {

        OxfordResponse oxfordResponseMock = new OxfordResponse();
        oxfordResponseMock.setCacheKey("cacheKey");

        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<WordApiResult>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        // Then
            String actual = cachedOxfordResponseRepositoryImp.findMeaningByCacheKey(oxfordResponseMock, "line");

            Assert.assertNull(actual);

        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );
    }

    @Test
    public void findMeaningByCacheKey_Success() {
                // Given
        OxfordResponse oxfordResponseMock = new OxfordResponse();
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
         //Then
        cachedOxfordResponseRepositoryImp.findMeaningByCacheKey(oxfordResponseMock, "line");

        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
                );
    }


    @Test
    public void findSynonymsByCacheKey_Fail_When_API404() {

        OxfordResponse oxfordResponseMock = new OxfordResponse();
        oxfordResponseMock.setCacheKey("cacheKey");

        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<WordApiResult>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        // Then
        String actual = cachedOxfordResponseRepositoryImp.findSynonymsByCacheKey(oxfordResponseMock, "line");

        Assert.assertNull(actual);

        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );
    }

    @Test
    public void findSynonymsByCacheKey_Success() {
        // Given
        OxfordResponse oxfordResponseMock = new OxfordResponse();
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

        //Then
        cachedOxfordResponseRepositoryImp.findSynonymsByCacheKey(oxfordResponseMock, "line");

        // Verify
        verify(restTemplate, times(1)).exchange(
                Mockito.anyString(),
                Mockito.<HttpMethod>eq(HttpMethod.GET),
                Mockito.<HttpEntity<?>>any(),
                Mockito.<Class<Object>>any()
        );
    }
}