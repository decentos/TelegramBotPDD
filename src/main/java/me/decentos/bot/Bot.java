package me.decentos.bot;

import lombok.SneakyThrows;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.service.OptionService;
import me.decentos.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    private final Button button;
    private final QuestionService questionService;
    private final OptionService optionService;

    @Autowired
    public Bot(Button button, QuestionService questionService, OptionService optionService) {
        this.button = button;
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @Override
    public String getBotUsername() {
        return "PddTgBot";
    }

    @Override
    public String getBotToken() {
        return "1265644920:AAFndR3wLswvzgdPlVcKErbKgslKcIq3MT4";
    }

    private List<Question> questions;
    private int questionNumber = 0;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        update.getMessage();
        if (message != null && message.hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = message.getText();
            if (text.equals("/start")) {
                sendMessage(chatId, "Выберите номер билета", message);
            } else if (text.matches("№\\s\\d*")) {
                int ticket = Integer.parseInt(text.substring(2));
                questions = questionService.findQuestionsByTicket(ticket);
                sendMessage(chatId, "Вы выбрали билет №" + text.substring(1), message);
                sendQuestion(chatId, questions.get(questionNumber));
            } else if (text.matches("\\d*")) {
                checkAnswer(chatId, text, questions.get(questionNumber));
                sendComment(chatId, questions.get(questionNumber));
                questionNumber++;
                sendQuestion(chatId, questions.get(questionNumber));
            }
        }
    }

    @SneakyThrows
    private synchronized void sendMessage(Long chatId, String text, Message message) {
        SendMessage sendMessage = sendMessageConfig(chatId, text);
        if (message.getText().equals("/start")) {
            button.setTicketButtons(sendMessage);
        }
        execute(sendMessage);
    }

    @SneakyThrows
    private synchronized void sendQuestion(Long chatId, Question question) {
        SendMessage sendQuestion = sendMessageConfig(chatId, question.getQuestionTitle());
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());
        button.setAnswerButtons(sendQuestion, options.size());
        execute(sendQuestion);
        for (Option o : options) {
            SendMessage sendOption = sendMessageConfig(chatId, o.getOptionTitle());
            execute(sendOption);
        }
    }

    @SneakyThrows
    private synchronized void checkAnswer(Long chatId, String text, Question question) {
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());
        int isCorrect = options.get(Integer.parseInt(text) - 1).getIsCorrect();
        int correctOption = options
                .indexOf(
                        options
                                .stream()
                                .filter(o -> o.getIsCorrect() == 1)
                                .findFirst().
                                orElseThrow()
                ) + 1;
        String answer = isCorrect == 1 ? "Верно!" : "Ошибка! Правильный ответ №" + correctOption;
        SendMessage sendAnswer = sendMessageConfig(chatId, answer);
        execute(sendAnswer);
    }

    @SneakyThrows
    private synchronized void sendComment(Long chatId, Question question) {
        SendMessage sendComment = sendMessageConfig(chatId, question.getComment());
        execute(sendComment);
    }

    private synchronized SendMessage sendMessageConfig(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
