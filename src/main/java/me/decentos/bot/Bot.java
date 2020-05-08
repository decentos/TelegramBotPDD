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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.util.List;

import static java.lang.Thread.sleep;

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

    // TODO Подгрузка фотографий для комментариев

    private List<Question> questions;
    private int questionNumber = 0;
    private int correctCount = 0;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        update.getMessage();
        Long chatId = update.getMessage().getChatId();
        String text = message.getText();

        if (text.equals("/start")) {
            sendMessage(chatId, "\uD83D\uDCA1 Выберите номер билета:", message);
        } else if (text.matches("№\\s\\d*")) {
            int ticket = Integer.parseInt(text.substring(2));
            questions = questionService.findQuestionsByTicket(ticket);
            sendMessage(chatId, "\uD83D\uDC8E Вы выбрали билет №" + text.substring(1), message);
            sendQuestion(chatId, questions.get(questionNumber));
        } else if (text.matches("\\d*")) {
            checkAnswer(chatId, text, questions.get(questionNumber));
            sendComment(chatId, questions.get(questionNumber));
            questionNumber++;
            if (questionNumber < questions.size()) {
                sendQuestion(chatId, questions.get(questionNumber));
            } else {
                sendResult(chatId, correctCount);
                questionNumber = 0;
                correctCount = 0;
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
        sleep(2_000);
    }

    @SneakyThrows
    private synchronized void sendQuestion(Long chatId, Question question) {
        String questionTitle = "❓" + question.getQuestionTitle();
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());

        if (question.getImage() != null) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(question.getId() + "-questionImage", new ByteArrayInputStream(question.getImage()));
            sendPhoto.setCaption(questionTitle);
            button.setAnswerButtonsByPhoto(sendPhoto, options.size());
            execute(sendPhoto);
        } else {
            SendMessage sendQuestion = sendMessageConfig(chatId, questionTitle);
            button.setAnswerButtons(sendQuestion, options.size());
            execute(sendQuestion);
        }
        sleep(2_500);
        for (Option o : options) {
            SendMessage sendOption = sendMessageConfig(chatId, o.getOptionTitle());
            execute(sendOption);
            sleep(1_500);
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
        String answer;
        if (isCorrect == 1) {
            correctCount++;
            answer = "✅ Верно!";
        } else {
            answer = "❌ Ошибка! Правильный ответ №" + correctOption;
        }
        SendMessage sendAnswer = sendMessageConfig(chatId, answer);
        execute(sendAnswer);
        sleep(1_000);
    }

    @SneakyThrows
    private synchronized void sendComment(Long chatId, Question question) {
        String comment = "\uD83D\uDCAD " + question.getComment();
        SendMessage sendComment = sendMessageConfig(chatId, comment);
        execute(sendComment);
        sleep(3_000);
    }

    @SneakyThrows
    private void sendResult(Long chatId, int correctCount) {
        String result;
        if (correctCount > 17) {
            result = "\uD83C\uDFC6 Поздравляем с успешно пройденным тестированием!\nКоличество правильных ответов: " + correctCount + " из 20 вопросов.";
        } else {
            result = "\uD83D\uDC4E Тестирование не пройдено!\nКоличество правильных ответов: " + correctCount + " из 20 вопросов.";
        }
        SendMessage sendResult = sendMessageConfig(chatId, result);
        button.setTicketButtons(sendResult);
        execute(sendResult);
    }

    private synchronized SendMessage sendMessageConfig(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
