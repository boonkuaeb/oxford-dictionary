package com.bk.dictionary.handler;


import com.bk.dictionary.services.OxfordService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

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

        oxfordService.translate(userId,originalMessageText);

    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }


}
