package me.decentos.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.decentos.bot.Bot;
import me.decentos.dto.UserDto;
import me.decentos.handler.RequestHandler;
import me.decentos.service.PrepareMessageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TestingRequestHandler implements RequestHandler {

    private final PrepareMessageService prepareMessageService;
    private final MessageSource messageSource;

    @SneakyThrows
    @Override
    public void handle(String text, Update update, Map<String, UserDto> users, Bot bot) {
        String testing = messageSource.getMessage("testing", null, Locale.getDefault());
        if (!text.equals(testing)) return;

        String selectTicket = messageSource.getMessage("select.ticket", null, Locale.getDefault());
        Long chatId = update.getMessage().getChatId();
        bot.execute(prepareMessageService.prepareMessage(chatId, selectTicket, text));
    }
}
