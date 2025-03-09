package se.kaninis.filemanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Konfigurationsklass för säkerhet och autentisering via OAuth2.
 */
@Configuration
public class SecurityConfig {

    /**
     * Definierar säkerhetsfilterkedjan för applikationen.
     *
     * @param http HttpSecurity-konfiguration.
     * @return SecurityFilterChain med konfigurerade säkerhetsregler.
     * @throws Exception Om konfigurationen misslyckas.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .requestMatchers("/login", "/oauth2/**").permitAll()
                .requestMatchers("/api/files/**", "/api/folders/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/api/auth/me", true)
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
            .cors(cors -> cors.configure(http)) // CORS-hantering
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/**")); // Skydda CSRF utom för OAuth2-anrop

        return http.build();
    }
}

/**
 * Controller för att returnera inloggad användares info från OAuth2.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    /**
     * Hämtar information om den inloggade användaren.
     *
     * @param authUser Den autentiserade OAuth2-användaren.
     * @return ResponseEntity med användarens attribut eller felmeddelande.
     */
    @GetMapping("/me")
    public Map<String, Object> getUserInfo(OAuth2User authUser) {
        return authUser.getAttributes();
    }
}
