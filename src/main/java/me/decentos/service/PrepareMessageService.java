package me.decentos.service;

import me.decentos.dto.UserDto;
import me.decentos.model.Option;
import me.decentos.model.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.List;
import java.util.Map;

public interface PrepareMessageService {
    SendMessage prepareMenu(Long chatId, String greeting);

    SendMessage prepareMessage(Long chatId, String answer, String text);

    SendMessage prepareQuestion(Long chatId, String question, int size);

    SendPhoto preparePhotoQuestion(Long chatId, Question question, int size);

    List<SendMessage> prepareOptions(Long chatId, List<Option> options);

    SendMessage checkAnswer(Long chatId, String text, List<Option> options, String userName, Map<String, UserDto> users);

    SendMessage prepareComment(Long chatId, Question question);

    SendMessage prepareResult(Long chatId, int correctCount);

    SendMessage prepareStatistics(Long chatId, String username);
}
