package com.example.modeltelegrambot.bots;

import com.example.modeltelegrambot.bots.bot.ApplicationBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class InitBot {
    final ApplicationBot applicationBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init(){
        try {
            TelegramBotsApi botsApi=new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(applicationBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
