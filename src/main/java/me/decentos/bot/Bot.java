package me.decentos.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.decentos.dto.UserDto;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.model.Ticket;
import me.decentos.service.OptionService;
import me.decentos.service.PrepareMessageService;
import me.decentos.service.QuestionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Thread.sleep;

@RequiredArgsConstructor
@Component
public class Bot extends TelegramLongPollingBot {

    private final PrepareMessageService prepareMessageService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final MessageSource messageSource;
    private final Map<String, UserDto> users = new HashMap<>();

    @Value("${bot.username}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
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
            execute(prepareMessageService.prepareMessage(chatId, selectTicket, text));
            sleep(1_000);
        } else if (text.matches("№\\s\\d*")) {
            sendAfterSelectTicket(chatId, text, userName, selectedTicket);
        } else if (text.matches("\\d*")) {
            sendAfterSelectOption(chatId, text, userName);
        }
    }

    @SneakyThrows
    private void sendAfterSelectTicket(Long chatId, String text, String userName, String selectedTicket) {
        if (!users.containsKey(userName)) {
            createUser(text, userName);
        }
        execute(prepareMessageService.prepareMessage(chatId, selectedTicket, null));
        sleep(1_000);
        UserDto user = users.get(userName);
        Question question = user.getQuestions().get(user.getQuestionNumber());
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());
        sendQuestion(chatId, question, options);
    }

    private void createUser(String text, String userName) {
        Ticket ticket = new Ticket(Integer.parseInt(text.substring(2)));
        List<Question> questions = questionService.findQuestionsByTicket(ticket);

        UserDto user = new UserDto();
        user.setUserName(userName);
        user.setTicket(ticket);
        user.setQuestions(questions);
        user.setQuestionNumber(0);
        user.setCorrectCount(0);
        users.put(userName, user);
    }

    @SneakyThrows
    private void sendAfterSelectOption(Long chatId, String text, String userName) {
        UserDto user = users.get(userName);
        int questionNumber = user.getQuestionNumber();
        Question question = user.getQuestions().get(questionNumber);
        List<Question> questions = user.getQuestions();
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());

        execute(prepareMessageService.checkAnswer(chatId, text, options, userName, users));
        sleep(500);
        execute(prepareMessageService.prepareComment(chatId, question));
        sleep(2_000);

        questionNumber++;
        user.setQuestionNumber(questionNumber);
        users.put(userName, user);

        if (questionNumber < questions.size()) {
            question = questions.get(questionNumber);
            options = optionService.findOptionsByQuestionId(question.getId());
            sendQuestion(chatId, question, options);
        } else {
            execute(prepareMessageService.prepareResult(chatId, users.get(userName).getCorrectCount()));
            users.remove(userName);
        }
    }

    @SneakyThrows
    private void sendQuestion(Long chatId, Question question, List<Option> options) {
        List<SendMessage> optionsList = prepareMessageService.prepareOptions(chatId, options);
        if (question.getImage() == null) {
            execute(prepareMessageService.prepareQuestion(chatId, question.getQuestionTitle(), options.size()));
        } else {
            execute(prepareMessageService.preparePhotoQuestion(chatId, question, options.size()));
        }
        sleep(1_000);
        for (SendMessage s : optionsList) {
            execute(s);
            sleep(300);
        }
    }
}
