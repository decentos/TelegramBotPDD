package me.decentos.service;

import me.decentos.dto.UserDto;
import me.decentos.model.Option;
import me.decentos.model.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.List;
import java.util.Map;

public interface SendMessageService {
    SendMessage sendMessage(Long chatId, String answer, String text);

    SendMessage sendQuestion(Long chatId, String question, int size);

    SendPhoto sendPhotoQuestion(Long chatId, Question question, int size);

    List<SendMessage> sendOptions(Long chatId, List<Option> options);

    SendMessage checkAnswer(Long chatId, String text, List<Option> options, String userName, Map<String, UserDto> users);

    SendMessage sendComment(Long chatId, Question question);

    SendMessage sendResult(Long chatId, int correctCount);
}
