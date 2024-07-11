package ru.project.SweetBot.bd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ru.project.SweetBot.bd.entity.Users;
import ru.project.SweetBot.bd.repository.UserRepository;

import java.util.List;

@Service
public class UsersManagementService {


    private final UserRepository userRepository;
    @Autowired
    public UsersManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(Users users) {
        userRepository.save(users);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

//    public Users getUser(String username) {
//        Users users = userRepository.findByUsername(username);
//        return users;
//    }

}
