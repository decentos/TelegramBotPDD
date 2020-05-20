package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.decentos.dto.UserDto;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.service.ButtonService;
import me.decentos.service.SendMessageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SendMessageServiceImpl implements SendMessageService {

    private final ButtonService buttonService;
    private final MessageSource messageSource;

    @SneakyThrows
    @Override
    public synchronized SendMessage sendMessage(Long chatId, String answer, String text) {
        SendMessage sendMessage = sendMessageConfig(chatId, answer);
        if (text != null) {
            buttonService.setTicketButtons(sendMessage);
        }
        return sendMessage;
    }

    @SneakyThrows
    @Override
    public synchronized SendMessage sendQuestion(Long chatId, String question, int size) {
        SendMessage sendQuestion = sendMessageConfig(chatId, String.format("❓%s", question));
        buttonService.setAnswerButtons(sendQuestion, size);
        return sendQuestion;
    }

    @SneakyThrows
    @Override
    public synchronized SendPhoto sendPhotoQuestion(Long chatId, Question question, int size) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(question.getId() + "-questionImage", new ByteArrayInputStream(question.getImage()));
        sendPhoto.setCaption(String.format("❓%s", question.getQuestionTitle()));
        buttonService.setAnswerButtonsByPhoto(sendPhoto, size);
        return sendPhoto;
    }

    @Override
    public synchronized List<SendMessage> sendOptions(Long chatId, List<Option> options) {
        List<SendMessage> optionsList = new ArrayList<>();
        for (Option o : options) {
            SendMessage sendOption = sendMessageConfig(chatId, o.getOptionTitle());
            optionsList.add(sendOption);
        }
        return optionsList;
    }

    @SneakyThrows
    @Override
    public synchronized SendMessage checkAnswer(Long chatId, String text, List<Option> options, String userName, Map<String, UserDto> users) {
        int isCorrect = options.get(Integer.parseInt(text) - 1).getIsCorrect();
        int correctOption = options
                .indexOf(
                        options
                                .stream()
                                .filter(o -> o.getIsCorrect() == 1)
                                .findFirst()
                                .orElseThrow(RuntimeException::new)
                ) + 1;
        String correctAnswer = messageSource.getMessage("correct.answer", null, Locale.getDefault());
        String incorrectAnswer = messageSource.getMessage("incorrect.answer", new Integer[]{correctOption}, Locale.getDefault());

        if (isCorrect == 1) {
            UserDto user = users.get(userName);
            int correctCount = user.getCorrectCount() + 1;
            user.setCorrectCount(correctCount);
            users.put(userName, user);
        }
        return sendMessageConfig(chatId, isCorrect == 1 ? correctAnswer : incorrectAnswer);
    }

    @SneakyThrows
    @Override
    public synchronized SendMessage sendComment(Long chatId, Question question) {
        String comment = "\uD83D\uDCAD " + question.getComment();
        return sendMessageConfig(chatId, comment);
    }

    @Override
    public synchronized SendMessage sendResult(Long chatId, int correctCount) {
        String passed = messageSource.getMessage("passed", new Integer[]{correctCount}, Locale.getDefault());
        String failure = messageSource.getMessage("failure", new Integer[]{correctCount}, Locale.getDefault());
        String result = correctCount > 17 ? passed : failure;

        SendMessage sendResult = sendMessageConfig(chatId, result);
        buttonService.setTicketButtons(sendResult);
        return sendResult;
    }

    private synchronized SendMessage sendMessageConfig(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
