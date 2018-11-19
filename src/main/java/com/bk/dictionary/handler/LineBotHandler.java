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
import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class LineBotHandler {


    private OxfordService oxfordService;

    @Autowired
    public LineBotHandler(OxfordService oxfordService) {
        this.oxfordService = oxfordService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        final String userId = event.getSource().getUserId();
        final String originalMessageText = event.getMessage().getText();
        System.out.println("userId: " + userId);
        System.out.println("originalMessageText: " + originalMessageText);

        oxfordService.getMeaning(userId,originalMessageText);

    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }


}
