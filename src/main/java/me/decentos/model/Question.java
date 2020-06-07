package me.decentos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int id;

    @Embedded
    private Ticket ticket;

    @Column(name = "question_title")
    private String questionTitle;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "comment")
    private String comment;
}
