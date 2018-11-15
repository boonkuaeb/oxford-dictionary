package com.bk.dictionary.handler;


import com.bk.dictionary.model.Synonyms;
import com.bk.dictionary.model.Word;
import com.bk.dictionary.services.WordService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class LineBotHandler {

    @Value("${line.bot.channel-token}")
    private String chanelToken;

    private WordService wordService;

    @Autowired
    public LineBotHandler(WordService phaseService) {
        this.wordService = phaseService;
    }


    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        final String replyToken = event.getReplyToken();
        final String userId = event.getSource().getUserId();
        final String originalMessageText = event.getMessage().getText();
        Word meaning = wordService.getMeaning(originalMessageText);
        replyTranslate(replyToken, meaning.getMean());

        Synonyms synonyms = wordService.getSynonyms(originalMessageText);
        if (synonyms.getStatus()) {
            pushSynonyms(userId, synonyms.getTextSynonyms());
        }

    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    private void replyTranslate(String replyToken, String text) {

        final LineMessagingClient client = LineMessagingClient
                .builder(chanelToken)
                .build();

        final TextMessage textMessage = new TextMessage(text);
        final ReplyMessage replyMessage = new ReplyMessage(
                replyToken,
                textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.replyMessage(replyMessage).get();
            System.out.println(botApiResponse);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            //return null;
        }
    }

    private void pushSynonyms(String userId, String text) {
        final TextMessage textMessage = new TextMessage(text);
        final LineMessagingClient client = LineMessagingClient
                .builder(chanelToken)
                .build();

        final PushMessage pushMessage = new PushMessage(
                userId,
                textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(botApiResponse);
    }

}
