package me.decentos.repository;

import me.decentos.model.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentImageRepository extends JpaRepository<CommentImage, Integer> {
}
