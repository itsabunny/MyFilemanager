package se.kaninis.filemanager.files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.kaninis.filemanager.users.UserEntity;
import se.kaninis.filemanager.users.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att ladda upp filer.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        if (file.getSize() > 10 * 1024 * 1024) { // Max 10MB
            return ResponseEntity.badRequest().body("Filstorleken får inte överstiga 10MB.");
        }

        try {
            fileService.saveFile(file.getOriginalFilename(), file.getBytes());
            return ResponseEntity.ok("Filen har laddats upp!");
        } catch (IOException | SQLException e) {
            return ResponseEntity.status(500).body("Fel vid uppladdning av fil.");
        }
    }
}
