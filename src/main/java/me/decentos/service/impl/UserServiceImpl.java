package me.decentos.service.impl;

import me.decentos.model.User;
import me.decentos.repository.UserRepository;
import me.decentos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public void saveUser(Long chatId, String username) {
        if (findByChatId(chatId) == null) {
            userRepository.save(new User(chatId, username));
        }
    }
}
