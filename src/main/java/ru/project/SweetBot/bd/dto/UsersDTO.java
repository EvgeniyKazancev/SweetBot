package ru.project.SweetBot.bd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsersDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String telegramId;

}
