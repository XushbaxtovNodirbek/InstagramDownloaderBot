package com.example.modeltelegrambot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BotConfig {

    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String botToken;
}
