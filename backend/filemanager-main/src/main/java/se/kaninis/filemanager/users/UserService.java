package se.kaninis.filemanager.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviceklass för hantering av användare.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Hittar en användare baserat på användarnamn.
     *
     * @param username Användarnamn.
     * @return Optional med användaren om den finns.
     */
    public Optional<UserEntity> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Användarnamn får inte vara tomt!");
        }
        return userRepository.findByUsername(username);
    }

    /**
     * Registrerar en ny användare om användarnamnet inte redan finns.
     *
     * @param username Användarnamn.
     * @return Den registrerade användaren.
     */
    @Transactional
    public UserEntity registerUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Användarnamn får inte vara tomt!");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Användarnamnet är redan registrerat!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        return userRepository.save(user);
    }
}
