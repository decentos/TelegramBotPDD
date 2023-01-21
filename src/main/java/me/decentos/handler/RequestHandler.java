package me.decentos.handler;

import me.decentos.bot.Bot;
import me.decentos.dto.UserDto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

public interface RequestHandler {

    void handle(String text, Update update, Map<String, UserDto> users, Bot bot) throws TelegramApiException;
}
