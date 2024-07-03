package ru.project.SweetBot.bd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ru.project.SweetBot.bd.entity.Users;
import ru.project.SweetBot.bd.repository.UserRepository;

import java.util.List;

@Service
public class UsersManagementService {
    @Autowired
    private final UserRepository userRepository;


    public UsersManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
  public void save(Users users) {
        userRepository.save(users);
  }
  public List<Users> findAll() {
        return userRepository.findAll();
  }


}
