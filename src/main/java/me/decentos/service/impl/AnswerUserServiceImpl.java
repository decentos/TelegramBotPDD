package me.decentos.service.impl;

import me.decentos.model.AnswerUser;
import me.decentos.model.Option;
import me.decentos.model.Question;
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

    @Override
    public AnswerUser findAnswerUserByUserAndQuestion(User user, Question question) {
        return answerUserRepository.findAnswerUserByUserAndQuestion(user, question);
    }

    @Override
    public void saveAnswerUser(User user, Option option) {
        AnswerUser update = findAnswerUserByUserAndQuestion(user, option.getQuestion());
        if (update == null) {
            answerUserRepository.save(new AnswerUser(user, option.getQuestion(), option));
        } else {
            update.setOption(option);
            answerUserRepository.save(update);
        }
    }
}
