package me.decentos.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface ButtonService {
    void setTicketButtons(SendMessage sendMessage);

    void setAnswerButtons(SendMessage sendMessage, int count);

    void setAnswerButtonsByPhoto(SendPhoto sendPhoto, int count);
}
