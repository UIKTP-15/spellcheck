package finki_ukim.spell_check_back_end.service;

import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String firstName, String lastName, String email, String password) {
        if (this.userRepository.existsByEmail(email)) {
            throw new RuntimeException(String.format("User with email %s already exists!", email));
        }
        User user = new User(firstName, lastName, email, password);
        return this.userRepository.save(user);
    }

    public boolean loginUser(String email, String password) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(String.format("User with email %s doesn't exist", email)));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Passwords do not match!");
        }
        return true;
    }
}
