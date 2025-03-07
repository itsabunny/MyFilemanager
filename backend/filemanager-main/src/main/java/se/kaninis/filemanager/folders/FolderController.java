package se.kaninis.filemanager.folders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    // Bytte från @Autowired till konstruktionsinjektion
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestParam String name, @RequestParam Long userId) {
        try {
            UserEntity user = new UserEntity(); // TODO: hämta användaren från databasen via UserService
            user.setId(userId);
            FolderEntity folder = folderService.createFolder(name, user);
            return ResponseEntity.ok(folder);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Kunde inte skapa mappen: " + e.getMessage()); // Felhantering
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFolders(@RequestParam Long userId) {
        try {
            UserEntity user = new UserEntity(); // TODO:️ hämta användaren från databasen via UserService
            user.setId(userId);
            List<FolderEntity> folders = folderService.getAllFolders(user).toList();
            return ResponseEntity.ok(folders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Fel vid hämtning av mappar: " + e.getMessage()); // Felhantering
        }
    }
}
