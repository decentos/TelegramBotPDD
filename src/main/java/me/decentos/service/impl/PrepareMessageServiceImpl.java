package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import me.decentos.dto.UserDto;
import me.decentos.model.AnswerUser;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.model.User;
import me.decentos.service.AnswerUserService;
import me.decentos.service.ButtonService;
import me.decentos.service.PrepareMessageService;
import me.decentos.service.UserService;
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
public class PrepareMessageServiceImpl implements PrepareMessageService {

    private final ButtonService buttonService;
    private final UserService userService;
    private final AnswerUserService answerUserService;
    private final MessageSource messageSource;

    @Override
    public synchronized SendMessage prepareMessage(Long chatId, String answer, String text) {
        SendMessage sendMessage = prepareMessageConfig(chatId, answer);
        if (text != null) {
            buttonService.setTicketButtons(sendMessage);
        }
        return sendMessage;
    }

    @Override
    public synchronized SendMessage prepareQuestion(Long chatId, String question, int size) {
        SendMessage sendQuestion = prepareMessageConfig(chatId, String.format("❓%s", question));
        buttonService.setAnswerButtons(sendQuestion, size);
        return sendQuestion;
    }

    @Override
    public synchronized SendPhoto preparePhotoQuestion(Long chatId, Question question, int size) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(question.getId() + "-questionImage", new ByteArrayInputStream(question.getImage()));
        sendPhoto.setCaption(String.format("❓%s", question.getQuestionTitle()));
        buttonService.setAnswerButtonsByPhoto(sendPhoto, size);
        return sendPhoto;
    }

    @Override
    public synchronized List<SendMessage> prepareOptions(Long chatId, List<Option> options) {
        List<SendMessage> optionsList = new ArrayList<>();
        for (Option o : options) {
            SendMessage sendOption = prepareMessageConfig(chatId, o.getOptionTitle());
            optionsList.add(sendOption);
        }
        return optionsList;
    }

    @Override
    public synchronized SendMessage checkAnswer(Long chatId, String text, List<Option> options, String userName, Map<String, UserDto> users) {
        User user = userService.findByUsername(userName);
        Option selectedOption = options.get(Integer.parseInt(text) - 1);
        answerUserService.saveAnswerUser(user, selectedOption);

        int isCorrect = selectedOption.getIsCorrect();
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
            UserDto userDto = users.get(userName);
            int correctCount = userDto.getCorrectCount() + 1;
            userDto.setCorrectCount(correctCount);
            users.put(userName, userDto);
        }
        return prepareMessageConfig(chatId, isCorrect == 1 ? correctAnswer : incorrectAnswer);
    }

    @Override
    public synchronized SendMessage prepareComment(Long chatId, Question question) {
        String comment = "\uD83D\uDCAD " + question.getComment();
        return prepareMessageConfig(chatId, comment);
    }

    @Override
    public synchronized SendMessage prepareResult(Long chatId, int correctCount) {
        String passed = messageSource.getMessage("passed", new Integer[]{correctCount}, Locale.getDefault());
        String failure = messageSource.getMessage("failure", new Integer[]{correctCount}, Locale.getDefault());
        String result = correctCount > 17 ? passed : failure;

        SendMessage sendResult = prepareMessageConfig(chatId, result);
        buttonService.setTicketButtons(sendResult);
        return sendResult;
    }

    @Override
    public synchronized SendMessage prepareStatistics(Long chatId, String userName) {
        User user = userService.findByUsername(userName);
        List<AnswerUser> answerUserByUser = answerUserService.findAnswerUserByUser(user);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 40; i++) {
            List<AnswerUser> byTicket = new ArrayList<>();
            for (AnswerUser a : answerUserByUser) {
                if (a.getOption().getQuestion().getTicket().getTicket() == i) {
                    byTicket.add(a);
                }
            }
            if (byTicket.size() == 0) {
                String ticketNoData = messageSource.getMessage("ticket.nodata", new Integer[]{i}, Locale.getDefault());
                sb.append(ticketNoData);
            } else {
                int correctAnswer = (int) byTicket.stream().filter(a -> a.getOption().getIsCorrect() == 1).count();
                String ticketPassed = messageSource.getMessage("ticket.passed", new Integer[]{i, correctAnswer}, Locale.getDefault());
                String ticketFailure = messageSource.getMessage("ticket.failure", new Integer[]{i, correctAnswer}, Locale.getDefault());
                sb.append(correctAnswer > 17 ? ticketPassed : ticketFailure);
            }
        }
        return prepareMessageConfig(chatId, sb.toString());
    }

    private synchronized SendMessage prepareMessageConfig(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
