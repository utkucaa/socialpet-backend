package com.example.social_pet.service;
import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    public User registerUser(UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }

        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        if (!userDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new RuntimeException("Invalid phone number format");
        }

        User newUser = new User();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setUserName(userDTO.getUserName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setPassword(userDTO.getPassword());

        return userRepository.save(newUser);
    }


}
