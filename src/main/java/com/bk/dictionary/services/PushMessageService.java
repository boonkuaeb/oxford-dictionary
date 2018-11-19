package com.bk.dictionary.services;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PushMessageService {


    @Value("${line.bot.channel-token}")
    private String chanelToken;

    public String pushMessage(String userId, String text) {
        final TextMessage textMessage = new TextMessage(text);
        System.out.println("chanelToken = " + chanelToken);
        final LineMessagingClient client = LineMessagingClient
                .builder(chanelToken)
                .build();

        final PushMessage pushMessage = new PushMessage(
                userId,
                textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
            return botApiResponse.getMessage();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }


    }
}
