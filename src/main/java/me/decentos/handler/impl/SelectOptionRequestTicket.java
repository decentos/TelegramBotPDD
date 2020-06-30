package me.decentos.handler.impl;

import lombok.RequiredArgsConstructor;
import me.decentos.bot.Bot;
import me.decentos.dto.UserDto;
import me.decentos.handler.RequestHandler;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.service.OptionService;
import me.decentos.service.PrepareMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class SelectOptionRequestTicket implements RequestHandler {

    private final PrepareMessageService prepareMessageService;
    private final OptionService optionService;

    @Override
    public void handle(String text, Update update, Map<String, UserDto> users, Bot bot) throws TelegramApiException {
        if (!text.matches("\\d*")) return;

        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getFrom().getUserName();
        sendAfterSelectOption(chatId, text, userName, users, bot);
    }

    private void sendAfterSelectOption(Long chatId, String text, String userName, Map<String, UserDto> users, Bot bot) throws TelegramApiException {
        UserDto user = users.get(userName);
        int questionNumber = user.getQuestionNumber();
        Question question = user.getQuestions().get(questionNumber);
        List<Question> questions = user.getQuestions();
        List<Option> options = optionService.findOptionsByQuestionId(question.getId());

        bot.execute(prepareMessageService.checkAnswer(chatId, text, options, userName, users));
        bot.execute(prepareMessageService.prepareComment(chatId, question));

        questionNumber++;
        user.setQuestionNumber(questionNumber);
        users.put(userName, user);

        if (questionNumber < questions.size()) {
            question = questions.get(questionNumber);
            options = optionService.findOptionsByQuestionId(question.getId());
            sendQuestion(chatId, question, options, bot);
        } else {
            bot.execute(prepareMessageService.prepareResult(chatId, users.get(userName).getCorrectCount()));
            users.remove(userName);
        }
    }

    private void sendQuestion(Long chatId, Question question, List<Option> options, Bot bot) throws TelegramApiException {
        SelectTicketRequestTicket.sendQuestion(chatId, question, options, bot, prepareMessageService);
    }
}
