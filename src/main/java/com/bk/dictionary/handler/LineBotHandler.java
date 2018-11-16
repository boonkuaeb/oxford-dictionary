package com.bk.dictionary.handler;


import com.bk.dictionary.model.OxfordResponse;
import com.bk.dictionary.services.OxfordService;
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

    private OxfordService oxfordService;

    @Autowired
    public LineBotHandler(OxfordService oxfordService) {
        this.oxfordService = oxfordService;
    }


    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        final String replyToken = event.getReplyToken();
        final String userId = event.getSource().getUserId();
        final String originalMessageText = event.getMessage().getText();
        OxfordResponse wordResponse = oxfordService.getMeaning(originalMessageText);
        if (wordResponse.getStatus()) {
            replyTranslate(replyToken, wordResponse.getText());
        } else {
            replyTranslate(replyToken, "Cannot find the meaning for this time.");
        }

        OxfordResponse synonyms = oxfordService.getSynonyms(originalMessageText);
        if (synonyms.getStatus()) {
            pushSynonyms(userId, synonyms.getText());
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
