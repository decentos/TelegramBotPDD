package me.decentos.service;

import me.decentos.model.AnswerUser;
import me.decentos.model.User;

import java.util.List;

public interface AnswerUserService {
    List<AnswerUser> findAnswerUserByUser(User user);
}
