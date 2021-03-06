package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.decentos.service.ButtonService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class ButtonServiceImpl implements ButtonService {

    private final MessageSource messageSource;

    @Override
    public void setMenuButtons(SendMessage sendMessage) {
        String testing = messageSource.getMessage("testing", null, Locale.getDefault());
        String statistic = messageSource.getMessage("statistic", null, Locale.getDefault());

        val replyKeyboardMarkup = new ReplyKeyboardMarkup();
        val keyboard = createKeyboardTemplate(replyKeyboardMarkup, sendMessage);
        val keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(testing));
        val keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton(statistic));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public void setTicketButtons(SendMessage sendMessage) {
        val replyKeyboardMarkup = new ReplyKeyboardMarkup();
        val keyboard = createKeyboardTemplate(replyKeyboardMarkup, sendMessage);

        val keyboardFirstRow = new KeyboardRow();
        fillKeyboard(keyboardFirstRow, 1, 8, true);

        val keyboardSecondRow = new KeyboardRow();
        fillKeyboard(keyboardSecondRow, 9, 8, true);

        val keyboardThirdRow = new KeyboardRow();
        fillKeyboard(keyboardThirdRow, 17, 8, true);

        val keyboardFourthRow = new KeyboardRow();
        fillKeyboard(keyboardFourthRow, 25, 8, true);

        val keyboardFifthRow = new KeyboardRow();
        fillKeyboard(keyboardFifthRow, 33, 8, true);

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);
        keyboard.add(keyboardFifthRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public void setAnswerButtons(SendMessage sendMessage, int count) {
        val replyKeyboardMarkup = new ReplyKeyboardMarkup();
        val keyboard = createKeyboardTemplate(replyKeyboardMarkup, sendMessage);
        createButtons(count, replyKeyboardMarkup, keyboard);
    }

    @Override
    public void setAnswerButtonsByPhoto(SendPhoto sendPhoto, int count) {
        val replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendPhoto.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        createButtons(count, replyKeyboardMarkup, keyboard);
    }

    private void createButtons(int count, ReplyKeyboardMarkup replyKeyboardMarkup, List<KeyboardRow> keyboard) {
        val keyboardFirstRow = new KeyboardRow();
        if (count <= 3) {
            fillKeyboard(keyboardFirstRow, 1, count, false);
            keyboard.add(keyboardFirstRow);
        } else {
            fillKeyboard(keyboardFirstRow, 1, 2, false);
            val keyboardSecondRow = new KeyboardRow();
            fillKeyboard(keyboardSecondRow, 3, count - 2, false);
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private List<KeyboardRow> createKeyboardTemplate(ReplyKeyboardMarkup replyKeyboardMarkup, SendMessage sendMessage) {
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return new ArrayList<>();
    }

    private void fillKeyboard(KeyboardRow keyboardRow, int initValue, int countButton, boolean isTicket) {
        for (int i = 0; i < countButton; i++) {
            if (isTicket) {
                keyboardRow.add(new KeyboardButton(String.format("№ %d", initValue)));
            } else {
                keyboardRow.add(new KeyboardButton(String.valueOf(initValue)));
            }
            initValue++;
        }
    }
}
