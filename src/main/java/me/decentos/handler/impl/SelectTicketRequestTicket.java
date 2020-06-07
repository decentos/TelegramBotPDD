package me.decentos.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.decentos.bot.Bot;
import me.decentos.dto.UserDto;
import me.decentos.handler.RequestHandler;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.model.Ticket;
import me.decentos.service.OptionService;
import me.decentos.service.PrepareMessageService;
import me.decentos.service.QuestionService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class SelectTicketRequestTicket implements RequestHandler {

    private final PrepareMessageService prepareMessageService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final MessageSource messageSource;

    @Override
    public void handle(String text, Update update, Map<String, UserDto> users, Bot bot) {
        if (!text.matches("№\\s\\d*")) return;

        String selectedTicket = messageSource.getMessage("selected.ticket", new String[]{text.substring(1)}, Locale.getDefault());
        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getFrom().getUserName();
        sendAfterSelectTicket(chatId, text, userName, selectedTicket, users, bot);
    }

    @SneakyThrows
    private void sendAfterSelectTicket(Long chatId, String text, String userName, String selectedTicket, Map<String, UserDto> users, Bot bot) {
        if (!users.containsKey(userName)) {
            createUser(text, userName, users);
        }
        bot.execute(prepareMessageService.prepareMessage(chatId, selectedTicket, null));
        UserDto user = users.get(userName);
        Question question = user.getQuestions().get(user.getQuestionNumber());
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());
        sendQuestion(chatId, question, options, bot);
    }

    private void createUser(String text, String userName, Map<String, UserDto> users) {
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
    private void sendQuestion(Long chatId, Question question, List<Option> options, Bot bot) {
        sendQuestion(chatId, question, options, bot, prepareMessageService);
    }

    public static void sendQuestion(Long chatId, Question question, List<Option> options, Bot bot, PrepareMessageService prepareMessageService) throws org.telegram.telegrambots.meta.exceptions.TelegramApiException {
        List<SendMessage> optionsList = prepareMessageService.prepareOptions(chatId, options);
        if (question.getImage() == null) {
            bot.execute(prepareMessageService.prepareQuestion(chatId, question.getQuestionTitle(), options.size()));
        } else {
            bot.execute(prepareMessageService.preparePhotoQuestion(chatId, question, options.size()));
        }
        for (SendMessage s : optionsList) {
            bot.execute(s);
        }
    }
}
