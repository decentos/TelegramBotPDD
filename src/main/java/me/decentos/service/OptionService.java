package me.decentos.service;

import me.decentos.model.Option;

import java.util.List;

public interface OptionService {
    List<Option> findOptionsByQuestionId(int questionId);
}
