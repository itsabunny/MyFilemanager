package se.kaninis.filemanager.folders;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import se.kaninis.filemanager.users.UserEntity;
import se.kaninis.filemanager.users.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;

    public FolderController(FolderService folderService, UserService userService) {
        this.folderService = folderService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestParam String name,
                                          @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att skapa en mapp.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        UserEntity user = userOpt.get();
        FolderEntity folder = folderService.createFolder(name, user);
        return ResponseEntity.ok(folder);
    }

    @GetMapping
    public ResponseEntity<?> getAllFolders(@AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att se dina mappar.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        UserEntity user = userOpt.get();
        List<FolderEntity> folders = folderService.getAllFolders(user).toList();
        return ResponseEntity.ok(folders);
    }
}
