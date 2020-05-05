package me.decentos.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.logging.Level;

import static org.telegram.telegrambots.logging.BotLogger.log;

public class Bot extends TelegramLongPollingBot {
    private final Button button = new Button();

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String text = message.getText();
            if (text.equals("/start")) {
                sendMessage(update.getMessage().getChatId().toString(), message, "Выберите номер билета");
            }
            else if (text.matches("№\\s\\d*")) {
                sendMessage(update.getMessage().getChatId().toString(), message, "Вы выбрали билет №" + text.substring(1));
            }
            else {
                sendMessage(update.getMessage().getChatId().toString(), message, text);
            }
        }
    }

    public synchronized void sendMessage(String chatId, Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            if (message.getText().equals("/start")) {
                button.setTicketButtons(sendMessage);
            }
            else if (message.getText().matches("№\\s\\d*")) {
                button.setAnswerButtons(sendMessage);
            }
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    @Override
    public String getBotUsername() {
        return "PddTgBot";
    }

    @Override
    public String getBotToken() {
        return "1265644920:AAFndR3wLswvzgdPlVcKErbKgslKcIq3MT4";
    }
}
