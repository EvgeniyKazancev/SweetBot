package ru.project.SweetBot.bd.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import ru.project.SweetBot.bd.entity.Users;
import ru.project.SweetBot.bd.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {

@Autowired
    private final UserRepository userRepository;

    @Autowired
    public UsersManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(Users users) {
        userRepository.save(users);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users getUser(Long userId) {
        Optional<Users> users = userRepository.findById(userId);
        return users.orElse(null);
    }
    public Long getUserId(Users users) {
       return users.getId();
    }
}
