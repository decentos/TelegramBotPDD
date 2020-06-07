package me.decentos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "option_title")
    private String optionTitle;

    @Column(name = "is_correct")
    private int isCorrect;
}
