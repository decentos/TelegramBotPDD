package me.decentos.service.impl;

import me.decentos.model.Option;
import me.decentos.repository.OptionRepository;
import me.decentos.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public List<Option> findOptionsByQuestionId(int questionId) {
        return optionRepository.findOptionsByQuestionId(questionId);
    }
}
