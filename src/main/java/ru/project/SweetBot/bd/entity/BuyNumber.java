package ru.project.SweetBot.bd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "bay_number")
public class BuyNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private int number;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "users_id",referencedColumnName = "id")
    private Users user;

    public BuyNumber() {
    }
}
