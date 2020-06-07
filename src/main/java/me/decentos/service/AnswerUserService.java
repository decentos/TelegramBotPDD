package me.decentos.service;

import me.decentos.model.AnswerUser;
import me.decentos.model.Option;
import me.decentos.model.Question;
import me.decentos.model.User;

import java.util.List;

public interface AnswerUserService {
    List<AnswerUser> findAnswerUserByUser(User user);

    AnswerUser findAnswerUserByUserAndQuestion(User user, Question question);

    void saveAnswerUser(User user, Option option);
}
