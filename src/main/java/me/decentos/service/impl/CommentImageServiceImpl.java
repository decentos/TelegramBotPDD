package me.decentos.service.impl;

import me.decentos.model.CommentImage;
import me.decentos.repository.CommentImageRepository;
import me.decentos.service.CommentImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentImageServiceImpl implements CommentImageService {

    private final CommentImageRepository commentImageRepository;

    @Autowired
    public CommentImageServiceImpl(CommentImageRepository commentImageRepository) {
        this.commentImageRepository = commentImageRepository;
    }

    @Override
    public List<CommentImage> findCommentImagesByQuestionId(int questionId) {
        return commentImageRepository.findCommentImagesByQuestionId(questionId);
    }
}
