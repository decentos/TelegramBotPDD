package me.decentos.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.telegram.telegrambots.logging.BotLogger.log;

public class Bot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String text = message.getText();
            if (text.equals("/start")) {
                sendMessage(update.getMessage().getChatId().toString(), message, "Выберите номер билета");
            }
            else if (text.matches("/\\d*")) {
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
                setTicketButtons(sendMessage);
            }
            else if (message.getText().matches("/\\d*")) {
                setAnswerButtons(sendMessage);
            }
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public synchronized void setTicketButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/1"));
        keyboardFirstRow.add(new KeyboardButton("/2"));
        keyboardFirstRow.add(new KeyboardButton("/3"));
        keyboardFirstRow.add(new KeyboardButton("/4"));
        keyboardFirstRow.add(new KeyboardButton("/5"));
        keyboardFirstRow.add(new KeyboardButton("/6"));
        keyboardFirstRow.add(new KeyboardButton("/7"));
        keyboardFirstRow.add(new KeyboardButton("/8"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("/9"));
        keyboardSecondRow.add(new KeyboardButton("/10"));
        keyboardSecondRow.add(new KeyboardButton("/11"));
        keyboardSecondRow.add(new KeyboardButton("/12"));
        keyboardSecondRow.add(new KeyboardButton("/13"));
        keyboardSecondRow.add(new KeyboardButton("/14"));
        keyboardSecondRow.add(new KeyboardButton("/15"));
        keyboardSecondRow.add(new KeyboardButton("/16"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("/17"));
        keyboardThirdRow.add(new KeyboardButton("/18"));
        keyboardThirdRow.add(new KeyboardButton("/19"));
        keyboardThirdRow.add(new KeyboardButton("/20"));
        keyboardThirdRow.add(new KeyboardButton("/21"));
        keyboardThirdRow.add(new KeyboardButton("/22"));
        keyboardThirdRow.add(new KeyboardButton("/23"));
        keyboardThirdRow.add(new KeyboardButton("/24"));

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add(new KeyboardButton("/25"));
        keyboardFourthRow.add(new KeyboardButton("/26"));
        keyboardFourthRow.add(new KeyboardButton("/27"));
        keyboardFourthRow.add(new KeyboardButton("/28"));
        keyboardFourthRow.add(new KeyboardButton("/29"));
        keyboardFourthRow.add(new KeyboardButton("/30"));
        keyboardFourthRow.add(new KeyboardButton("/31"));
        keyboardFourthRow.add(new KeyboardButton("/32"));

        KeyboardRow keyboardFifthRow = new KeyboardRow();
        keyboardFifthRow.add(new KeyboardButton("/33"));
        keyboardFifthRow.add(new KeyboardButton("/34"));
        keyboardFifthRow.add(new KeyboardButton("/35"));
        keyboardFifthRow.add(new KeyboardButton("/36"));
        keyboardFifthRow.add(new KeyboardButton("/37"));
        keyboardFifthRow.add(new KeyboardButton("/38"));
        keyboardFifthRow.add(new KeyboardButton("/39"));
        keyboardFifthRow.add(new KeyboardButton("/40"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);
        keyboard.add(keyboardFifthRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized void setAnswerButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("1"));
        keyboardFirstRow.add(new KeyboardButton("2"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("3"));
        keyboardSecondRow.add(new KeyboardButton("4"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
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
