package ru.project.SweetBot.bd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Email
    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber", unique = true)
    private Long phoneNumber;

    @Column(name = "Telegram", unique = true)
    private String telegramId;

    @Column(name = "Admin", nullable = false, length = 1)
    private boolean isAdmin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BuyNumber> bayNumbers;

   
    public Users(String firstName, String lastName, Long phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Users() {
    }

}
