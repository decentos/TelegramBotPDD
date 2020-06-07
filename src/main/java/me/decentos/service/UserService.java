package me.decentos.service;

import me.decentos.model.User;

public interface UserService {
    User findByChatId(Long chatId);

    void saveUser(Long chatId, String username);
}
