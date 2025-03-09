package se.kaninis.filemanager.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller för hantering av användare.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Hämtar information om den inloggade användaren.
     *
     * @param authUser OAuth2-användaren.
     * @return Användaruppgifter eller felmeddelande.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Ingen användare är inloggad.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad i databasen.");
        }

        return ResponseEntity.ok(userOpt.get());
    }

    /**
     * Registrerar en ny användare.
     *
     * @param username Användarnamn som skickas via query-param.
     * @return Registrerad användare eller felmeddelande.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Användarnamn får inte vara tomt!");
        }

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Användarnamnet är redan registrerat!");
        }

        UserEntity newUser = userService.registerUser(username);
        return ResponseEntity.ok(newUser);
    }
}
