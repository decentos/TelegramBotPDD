package me.decentos.service.impl;

import me.decentos.model.AnswerUser;
import me.decentos.model.User;
import me.decentos.repository.AnswerUserRepository;
import me.decentos.service.AnswerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerUserServiceImpl implements AnswerUserService {

    private final AnswerUserRepository answerUserRepository;

    @Autowired
    public AnswerUserServiceImpl(AnswerUserRepository answerUserRepository) {
        this.answerUserRepository = answerUserRepository;
    }

    @Override
    public List<AnswerUser> findAnswerUserByUser(User user) {
        return answerUserRepository.findAnswerUserByUser(user);
    }
}
