package ru.project.SweetBot.bd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber", unique = true)
    private Long phoneNumber;

    @Column(name = "telegram", unique = true)
    private String telegramId;

    @Column(name = "admin", nullable = false, length = 1)
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
