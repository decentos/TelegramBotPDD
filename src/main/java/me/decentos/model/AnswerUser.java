package me.decentos.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "answer_user")
public class AnswerUser {

    @Id
    @Column(name = "answer_user_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NonNull
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    @NonNull
    private Option option;
}
