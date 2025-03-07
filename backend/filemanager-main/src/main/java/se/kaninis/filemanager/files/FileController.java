package se.kaninis.filemanager.files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
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

    @GetMapping("/{fileId}")
    public ResponseEntity<Optional<byte[]>> downloadFile(@PathVariable Long fileId) {
        try {
            Optional<byte[]> content = fileService.getFileContent(fileId);
            if (content == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file_" + fileId + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        boolean deleted = fileService.deleteFile(fileId);

        if (deleted) {
            return ResponseEntity.ok("Filen har raderats!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
