package se.kaninis.filemanager.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
}
