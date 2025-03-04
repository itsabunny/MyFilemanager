package se.kaninis.filemanager.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.kaninis.filemanager.folders.FolderEntity;
import se.kaninis.filemanager.folders.FolderRepository;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderRepository folderRepository;

    @PostMapping("upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("folderId") Long folderId) {
        Optional<FolderEntity> folderOpt = folderRepository.findById(folderId);

        if (folderOpt.isEmpty()) {
         return ResponseEntity.badRequest().body("Mappen finns inte.");
        }

        try {
            FileEntity fileEntity = new FileEntity(file.getOriginalFilename(), file.getBytes(), folderOpt.get());
            fileService.saveFile(fileEntity);
            return ResponseEntity.ok("Filen har laddats upp!");
        } catch (IOException e){
            return ResponseEntity.status(500).body("Fel vid uppladdning av fil.");
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId){
        Optional<FileEntity> fileOpt = fileService.getFileById(fileId);

        if (fileOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        FileEntity file = fileOpt.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getContent());
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId){
        boolean deleted = fileService.deleteFile(fileId);

        if (deleted){
            return ResponseEntity.ok("Filen raderad!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
