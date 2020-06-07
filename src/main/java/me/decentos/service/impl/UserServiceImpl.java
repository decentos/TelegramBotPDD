package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import me.decentos.model.User;
import me.decentos.repository.UserRepository;
import me.decentos.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
