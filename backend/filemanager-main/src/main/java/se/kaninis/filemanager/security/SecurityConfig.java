package se.kaninis.filemanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/register", "/login").permitAll()
                .requestMatchers("/api/files/**", "/api/folders/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/api/auth/me", true) // Byter till /api/auth/me för att frontend kan få användardata
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
            .cors(cors -> cors.disable()) // Stäng av CORS här om du hanterar det på annat sätt
            .csrf(csrf -> csrf.disable()); // Stäng av CSRF för API-anrop
        return http.build();
    }
}

/**
 * Controller för att returnera inloggad användares info från OAuth2.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Ingen användare är inloggad.");
        }
        return ResponseEntity.ok(authUser.getAttributes());
    }
}

