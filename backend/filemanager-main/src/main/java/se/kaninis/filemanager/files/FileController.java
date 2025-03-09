package se.kaninis.filemanager.files;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import java.util.Map;
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

    /**
     * Laddar upp en fil kopplad till den inloggade användaren.
     *
     * @param file     Filen som laddas upp.
     * @param authUser Inloggad användare via OAuth2.
     * @return ResponseEntity med status och länk till uppladdad fil.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att ladda upp filer.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Filstorleken får inte överstiga 10MB.");
        }

        try {
            Long fileId = fileService.saveFile(file.getOriginalFilename(), file.getBytes());
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FileController.class)
                    .getFile(fileId, authUser)).withSelfRel();

            return ResponseEntity.ok(Map.of(
                    "message", "Filen har laddats upp!",
                    "fileLink", selfLink.toUri().toString()
            ));
        } catch (IOException | SQLException e) {
            return ResponseEntity.internalServerError().body("Fel vid uppladdning.");
        }
    }

    /**
     * Hämtar innehållet av en fil som tillhör användaren.
     *
     * @param id       Filens ID.
     * @param authUser Inloggad användare via OAuth2.
     * @return ResponseEntity med filens innehåll.
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id, @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            Optional<byte[]> fileContent = fileService.getFileContent(id);
            if (fileContent.isPresent()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.bin\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(fileContent.get());
            }
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Raderar en fil om den tillhör användaren.
     *
     * @param id       Filens ID.
     * @param authUser Inloggad användare via OAuth2.
     * @return ResponseEntity med status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id, @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att radera filer.");
        }

        boolean deleted = fileService.deleteFile(id);
        if (deleted) {
            return ResponseEntity.ok("Filen har raderats.");
        } else {
            return ResponseEntity.status(404).body("Filen kunde inte hittas eller tillhör inte dig.");
        }
    }
}
