package com.bk.dictionary.services;

import com.bk.dictionary.model.OxfordResponse;
import com.bk.dictionary.repository.CachedOxfordResponseRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OxfordServiceTest {

    @Mock
    CachedOxfordResponseRepository cachedOxfordResponseRepositoryImp;

    @Mock
    PushMessageService pushMessageService;

    @InjectMocks
    private OxfordService oxfordService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oxfordService = new OxfordService(pushMessageService, cachedOxfordResponseRepositoryImp);

    }

    @Test
    public void translate_Fail_When_InputBlank() {

        try {
            oxfordService.translate("TESt", "");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof Exception);

        }
    }

    @Test
    public void translate_Fail_When_ApiReturn404() {
        // When
        when(
                cachedOxfordResponseRepositoryImp.findMeaningByCacheKey
                        (
                                Mockito.any(OxfordResponse.class),
                                Mockito.anyString()
                        )
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));


        // Then
        try {
            oxfordService.translate("userid", "line");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof HttpClientErrorException);
        }
        // Verify
        verify(cachedOxfordResponseRepositoryImp, times(1)).findMeaningByCacheKey(
                Mockito.any(OxfordResponse.class),
                Mockito.anyString()
        );
    }

    @Test
    public void translate_Success() {
        //
        // When
        when(
                cachedOxfordResponseRepositoryImp.findMeaningByCacheKey
                        (
                                Mockito.any(OxfordResponse.class),
                                Mockito.anyString()
                        )
        ).thenReturn("line");

        when(
            cachedOxfordResponseRepositoryImp.findSynonymsByCacheKey
                    (
                            Mockito.any(OxfordResponse.class),
                            Mockito.anyString()
                    )
        ).thenReturn("Outline, Sketch");

        when(
                pushMessageService.pushMessage(
                        Mockito.anyString(),
                        Mockito.anyString()
                )
        ).thenReturn("Message");


        oxfordService.translate("userid", "line");

        verify(cachedOxfordResponseRepositoryImp, times(1)).findMeaningByCacheKey(
                Mockito.any(OxfordResponse.class),
                Mockito.anyString()
        );

        verify(cachedOxfordResponseRepositoryImp, times(1)).findSynonymsByCacheKey(
                Mockito.any(OxfordResponse.class),
                Mockito.anyString()
        );

        verify(pushMessageService, times(2)).pushMessage(
                Mockito.anyString(),
                Mockito.anyString()
        );
    }



    @Test
    public void pushSynonymsMessage_Fail_When_ApiReturn404() {
        // When
        when(
                cachedOxfordResponseRepositoryImp.findSynonymsByCacheKey
                        (
                                Mockito.any(OxfordResponse.class),
                                Mockito.anyString()
                        )
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));


        // Then
        try {
            oxfordService.pushSynonymsMessage("userid", "line");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof HttpClientErrorException);
        }
        // Verify
        verify(cachedOxfordResponseRepositoryImp, times(1)).findSynonymsByCacheKey(
                Mockito.any(OxfordResponse.class),
                Mockito.anyString()
        );
    }


}