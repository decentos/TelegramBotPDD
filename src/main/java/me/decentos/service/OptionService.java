package me.decentos.service;

import me.decentos.model.Option;
import me.decentos.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public List<Option> findOptionsByQuestionId(int questionId) {
        return optionRepository.findOptionsByQuestionId(questionId);
    }
}
