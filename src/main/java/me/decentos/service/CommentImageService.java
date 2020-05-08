package me.decentos.service;

import me.decentos.model.CommentImage;
import me.decentos.repository.CommentImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentImageService {

    private final CommentImageRepository commentImageRepository;

    @Autowired
    public CommentImageService(CommentImageRepository commentImageRepository) {
        this.commentImageRepository = commentImageRepository;
    }

    public List<CommentImage> findCommentImagesByQuestionId(int questionId) {
        return commentImageRepository.findCommentImagesByQuestionId(questionId);
    }
}
