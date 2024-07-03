package ru.project.SweetBot.bd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.project.SweetBot.bd.entity.Users;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BuyNumberDTO {
     private int number;
     private LocalDateTime dateTime;
     private Users users;
}
