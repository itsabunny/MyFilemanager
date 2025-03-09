package se.kaninis.filemanager.users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository för hantering av användare i databasen.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Hittar en användare baserat på användarnamn.
     *
     * @param username Användarnamn.
     * @return Optional med användaren om den finns.
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Kontrollerar om en användare med det angivna användarnamnet existerar.
     *
     * @param username Användarnamn.
     * @return true om användaren finns, annars false.
     */
    boolean existsByUsername(String username);
}
