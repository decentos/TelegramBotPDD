package me.decentos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "username")
    private String username;

    public User(long chatId, String username) {
        this.chatId = chatId;
        this.username = username;
    }
}
