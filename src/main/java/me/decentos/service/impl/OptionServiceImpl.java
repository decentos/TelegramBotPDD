package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import me.decentos.model.Option;
import me.decentos.repository.OptionRepository;
import me.decentos.service.OptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Override
    public List<Option> findOptionsByQuestionId(int questionId) {
        return optionRepository.findOptionsByQuestionId(questionId);
    }
}
