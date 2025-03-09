package se.kaninis.filemanager.users;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
     * @param authUser Den autentiserade användaren.
     * @return ResponseEntity med användardata eller felmeddelande.
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

        UserEntity user = userOpt.get();
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getCurrentUser(authUser)).withSelfRel();

        return ResponseEntity.ok().body(user).header("Link", selfLink.toUri().toString());
    }

    /**
     * Registrerar en ny användare.
     *
     * @param authUser Den autentiserade användaren via OAuth2.
     * @return ResponseEntity med registrerad användare eller felmeddelande.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Autentisering krävs för registrering.");
        }

        String username = authUser.getAttribute("name");
        if (userService.existsByUsername(username)) {
            return ResponseEntity.status(409).body("Användarnamnet är redan registrerat.");
        }

        UserEntity newUser = userService.registerUser(username);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getCurrentUser(authUser)).withSelfRel();

        return ResponseEntity.ok().body(newUser).header("Link", selfLink.toUri().toString());
    }
}
