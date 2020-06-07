package me.decentos.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.decentos.dto.UserDto;
import me.decentos.handler.RequestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class Bot extends TelegramLongPollingBot {

    private final List<RequestHandler> handlers;
    private final Map<String, UserDto> users = new HashMap<>();

    @Value("${bot.username}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        handlers.forEach(h -> h.handle(text, update, users, this));
    }
}
