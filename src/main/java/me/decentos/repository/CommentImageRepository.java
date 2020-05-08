package me.decentos.repository;

import me.decentos.model.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentImageRepository extends JpaRepository<CommentImage, Integer> {
    List<CommentImage> findCommentImagesByQuestionId(int questionId);
}
