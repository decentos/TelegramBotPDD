package me.decentos.bot;

import lombok.SneakyThrows;
import me.decentos.dto.UserDto;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.service.ButtonService;
import me.decentos.service.OptionService;
import me.decentos.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Thread.sleep;

@Component
public class Bot extends TelegramLongPollingBot {

    private final ButtonService buttonService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final Map<String, UserDto> users = new HashMap<>();
    private final MessageSource messageSource;

    @Value("${bot.username}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    public Bot(ButtonService buttonService, QuestionService questionService, OptionService optionService,
               MessageSource messageSource) {
        this.buttonService = buttonService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.messageSource = messageSource;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        update.getMessage();
        Long chatId = update.getMessage().getChatId();
        String text = message.getText();
        String userName = update.getMessage().getFrom().getUserName();

        String start = messageSource.getMessage("start", null, Locale.getDefault());
        String end = messageSource.getMessage("end", null, Locale.getDefault());
        String selectTicket = messageSource.getMessage("select.ticket", null, Locale.getDefault());
        String selectedTicket = messageSource.getMessage("selected.ticket", new String[]{text.substring(1)}, Locale.getDefault());

        if (text.equals(start) || text.equals(end)) {
            users.remove(userName);
            sendMessage(chatId, selectTicket, text);
        } else if (text.matches("№\\s\\d*")) {
            if (!users.containsKey(userName)) {
                int ticket = Integer.parseInt(text.substring(2));
                List<Question> questions = questionService.findQuestionsByTicket(ticket);

                UserDto user = new UserDto();
                user.setUserName(userName);
                user.setTicket(ticket);
                user.setQuestions(questions);
                user.setQuestionNumber(0);
                user.setCorrectCount(0);
                users.put(userName, user);
            }
            sendMessage(chatId, selectedTicket, null);
            UserDto user = users.get(userName);
            Question question = user.getQuestions().get(user.getQuestionNumber());
            sendQuestion(chatId, question);
        } else if (text.matches("\\d*")) {
            UserDto user = users.get(userName);
            int questionNumber = user.getQuestionNumber();
            Question question = user.getQuestions().get(questionNumber);
            List<Question> questions = user.getQuestions();

            checkAnswer(chatId, text, question, userName);
            sendComment(chatId, question);

            questionNumber++;
            user.setQuestionNumber(questionNumber);
            users.put(userName, user);

            if (questionNumber < questions.size()) {
                sendQuestion(chatId, questions.get(questionNumber));
            } else {
                sendResult(chatId, users.get(userName).getCorrectCount());
                users.remove(userName);
            }
        }
    }

    @SneakyThrows
    private synchronized void sendMessage(Long chatId, String answer, String text) {
        SendMessage sendMessage = sendMessageConfig(chatId, answer);
        if (text != null) {
            buttonService.setTicketButtons(sendMessage);
        }
        execute(sendMessage);
        sleep(1_000);
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
            buttonService.setAnswerButtonsByPhoto(sendPhoto, options.size());
            execute(sendPhoto);
        } else {
            SendMessage sendQuestion = sendMessageConfig(chatId, questionTitle);
            buttonService.setAnswerButtons(sendQuestion, options.size());
            execute(sendQuestion);
        }
        sleep(1_000);
        for (Option o : options) {
            SendMessage sendOption = sendMessageConfig(chatId, o.getOptionTitle());
            execute(sendOption);
            sleep(300);
        }
    }

    @SneakyThrows
    private synchronized void checkAnswer(Long chatId, String text, Question question, String userName) {
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());
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
        SendMessage sendAnswer = sendMessageConfig(chatId, isCorrect == 1 ? correctAnswer : incorrectAnswer);
        execute(sendAnswer);
        sleep(500);
    }

    @SneakyThrows
    private synchronized void sendComment(Long chatId, Question question) {
        String comment = "\uD83D\uDCAD " + question.getComment();
        SendMessage sendComment = sendMessageConfig(chatId, comment);
        execute(sendComment);
        sleep(2_000);
    }

    @SneakyThrows
    private void sendResult(Long chatId, int correctCount) {
        String passed = messageSource.getMessage("passed", new Integer[]{correctCount}, Locale.getDefault());
        String failure = messageSource.getMessage("failure", new Integer[]{correctCount}, Locale.getDefault());
        String result = correctCount > 17 ? passed : failure;

        SendMessage sendResult = sendMessageConfig(chatId, result);
        buttonService.setTicketButtons(sendResult);
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
