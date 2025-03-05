package se.kaninis.filemanager.files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.kaninis.filemanager.folders.FolderEntity;
import se.kaninis.filemanager.folders.FolderRepository;
import se.kaninis.filemanager.users.UserEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final FolderRepository folderRepository;

    public FileController(FileService fileService, FolderRepository folderRepository) {
        this.fileService = fileService;
        this.folderRepository = folderRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("folderId") Long folderId,
                                             @RequestParam("userId") Long userId) {
        Optional<FolderEntity> folderOpt = folderRepository.findById(folderId);

        if (folderOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Mappen finns inte.");
        }

        try {
            UserEntity user = new UserEntity();  // Simulera användare
            user.setId(userId);  // I verklig kod: hämta från säkerhetskontexten

            fileService.saveFile(file.getOriginalFilename(), file.getBytes(), folderId, user);
            return ResponseEntity.ok("Filen har laddats upp!");
        } catch (IOException | SQLException e) {
            return ResponseEntity.status(500).body("Fel vid uppladdning av fil.");
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId,
                                               @RequestParam("userId") Long userId) throws SQLException {
        UserEntity user = new UserEntity();  // Simulera användare
        user.setId(userId);  // I verklig kod: hämta från säkerhetskontexten

        byte[] fileContent = fileService.getFileContent(fileId, user);

        if (fileContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId,
                                             @RequestParam("userId") Long userId) {
        UserEntity user = new UserEntity();  // Simulera användare
        user.setId(userId);  // I verklig kod: hämta från säkerhetskontexten

        boolean deleted = fileService.deleteFile(fileId, user);

        if (deleted) {
            return ResponseEntity.ok("Filen raderad!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
