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
public class StatisticRequestHandler implements RequestHandler {

    private final PrepareMessageService prepareMessageService;
    private final MessageSource messageSource;

    @SneakyThrows
    @Override
    public void handle(String text, Update update, Map<String, UserDto> users, Bot bot) {
        String statistic = messageSource.getMessage("statistic", null, Locale.getDefault());
        if (!text.equals(statistic)) return;

        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getFrom().getUserName();
        bot.execute(prepareMessageService.prepareStatistics(chatId, userName));
    }
}
