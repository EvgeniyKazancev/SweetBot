package ru.project.SweetBot.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.project.SweetBot.bd.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    //Users findByUsername(String username);
}
