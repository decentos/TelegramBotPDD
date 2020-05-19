package me.decentos.service;

import me.decentos.model.CommentImage;

import java.util.List;

public interface CommentImageService {
    List<CommentImage> findCommentImagesByQuestionId(int questionId);
}
