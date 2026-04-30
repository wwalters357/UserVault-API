package com.walters.uservault_api.service;

import com.walters.uservault_api.dto.CreateUserRequest;
import com.walters.uservault_api.dto.UpdateUserRequest;
import com.walters.uservault_api.exception.UserNotFoundException;
import com.walters.uservault_api.model.User;
import com.walters.uservault_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(CreateUserRequest request) {
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getRole()
        );

        return userRepository.save(user);
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        User user = getUserById(id);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public List<User> searchByLastName(String lastName) {
        return userRepository.findByLastNameIgnoreCase(lastName);
    }

}
