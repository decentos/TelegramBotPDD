package me.decentos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question_comment_image")
public class CommentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment_image")
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "image")
    private byte[] image;
}
