package me.decentos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.decentos.model.Question;
import me.decentos.model.Ticket;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userName;
    private Ticket ticket;
    private List<Question> questions;
    private int questionNumber;
    private int correctCount;
}
