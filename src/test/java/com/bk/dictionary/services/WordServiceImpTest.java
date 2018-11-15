package com.bk.dictionary.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class WordServiceImpTest {
    private WordServiceImp phaseService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Before
    public void tearUp() {
        MockitoAnnotations.initMocks(this);
        phaseService = new WordServiceImp(restTemplateBuilder);
    }

    @Test
    public void extractSynonymsShouldReturnArrayWhenSplit() {
        // Given
        String[] expectArray = new String[5];
        // When
        String[] actual = phaseService.extractSynonyms(initInputText());

        // Then
        Assert.assertEquals(expectArray.length, actual.length);
    }

    @Test
    public void extractSynonymsShouldReturnExactlyWord() {

        String[] actual = phaseService.extractSynonyms(initInputText());
        Assert.assertEquals("knowledge", actual[0]);

    }

    private String initInputText() {
        return "{\n" +
                "    \"metadata\": {\n" +
                "        \"provider\": \"Oxford University Press\"\n" +
                "    },\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"id\": \"information\",\n" +
                "            \"language\": \"en\",\n" +
                "            \"lexicalEntries\": [\n" +
                "                {\n" +
                "                    \"entries\": [\n" +
                "                        {\n" +
                "                            \"homographNumber\": \"000\",\n" +
                "                            \"senses\": [\n" +
                "                                {\n" +
                "                                    \"examples\": [\n" +
                "                                        {\n" +
                "                                            \"text\": \"for further information write to the address below\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"id\": \"t_en_gb0007830.001\",\n" +
                "                                    \"subsenses\": [\n" +
                "                                        {\n" +
                "                                            \"id\": \"idf2f91267-57e1-4036-84fc-9f4847b02dfa\",\n" +
                "                                            \"synonyms\": [\n" +
                "                                                {\n" +
                "                                                    \"id\": \"knowledge\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"knowledge\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"intelligence\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"intelligence\"\n" +
                "                                                }\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"id372d79ba-84c4-4bb6-b15d-e4c36a0757e5\",\n" +
                "                                            \"synonyms\": [\n" +
                "                                                {\n" +
                "                                                    \"id\": \"instruction\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"instruction\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"advice\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"advice\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"guidance\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"guidance\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"direction\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"direction\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"counsel\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"counsel\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"enlightenment\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"enlightenment\"\n" +
                "                                                }\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"id83b89bf5-c64a-4000-af03-161e8651ab4f\",\n" +
                "                                            \"synonyms\": [\n" +
                "                                                {\n" +
                "                                                    \"id\": \"news\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"news\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"notice\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"notice\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"word\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"word\"\n" +
                "                                                }\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"id87091739-c5bf-4461-9340-fcc2b727b004\",\n" +
                "                                            \"synonyms\": [\n" +
                "                                                {\n" +
                "                                                    \"id\": \"material\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"material\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"documentation\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"documentation\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"documents\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"documents\"\n" +
                "                                                }\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"id43707ec6-1f23-4aa5-adb8-2ae66b490817\",\n" +
                "                                            \"registers\": [\n" +
                "                                                \"informal\"\n" +
                "                                            ],\n" +
                "                                            \"synonyms\": [\n" +
                "                                                {\n" +
                "                                                    \"id\": \"info\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"info\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"gen\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"gen\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"the_low-down\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"the low-down\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"the_dope\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"the dope\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"the_inside_story\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"the inside story\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"the_latest\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"the latest\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"bumf\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"bumf\"\n" +
                "                                                },\n" +
                "                                                {\n" +
                "                                                    \"id\": \"deets\",\n" +
                "                                                    \"language\": \"en\",\n" +
                "                                                    \"text\": \"deets\"\n" +
                "                                                }\n" +
                "                                            ]\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"synonyms\": [\n" +
                "                                        {\n" +
                "                                            \"id\": \"details\",\n" +
                "                                            \"language\": \"en\",\n" +
                "                                            \"text\": \"details\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"particulars\",\n" +
                "                                            \"language\": \"en\",\n" +
                "                                            \"text\": \"particulars\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"facts\",\n" +
                "                                            \"language\": \"en\",\n" +
                "                                            \"text\": \"facts\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"figures\",\n" +
                "                                            \"language\": \"en\",\n" +
                "                                            \"text\": \"figures\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"statistics\",\n" +
                "                                            \"language\": \"en\",\n" +
                "                                            \"text\": \"statistics\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"id\": \"data\",\n" +
                "                                            \"language\": \"en\",\n" +
                "                                            \"text\": \"data\"\n" +
                "                                        }\n" +
                "                                    ]\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"language\": \"en\",\n" +
                "                    \"lexicalCategory\": \"Noun\",\n" +
                "                    \"text\": \"information\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"type\": \"headword\",\n" +
                "            \"word\": \"information\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}