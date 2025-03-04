package se.kaninis.filemanager.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<OAuth2User> getCurrentUser(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok(user);
    }
}
