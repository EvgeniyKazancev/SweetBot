package ru.project.SweetBot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.project.SweetBot.bot.TelegramSweetBot;

@Component
public class BotInitializer {
    private final TelegramSweetBot telegramSweetBot;

    @Autowired
    public BotInitializer(TelegramSweetBot telegramSweetBot) {
        this.telegramSweetBot = telegramSweetBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramSweetBot);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
