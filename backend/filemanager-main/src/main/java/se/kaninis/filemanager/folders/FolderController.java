package se.kaninis.filemanager.folders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<FolderEntity> createFolder(@RequestParam String name){
        FolderEntity folder = folderService.createFolder(name);
        return ResponseEntity.ok(folder);
    }

    @GetMapping
    public ResponseEntity<List<FolderEntity>> getAllFolders(){
        List<FolderEntity> folders = folderService.getAllFolders();
        return ResponseEntity.ok(folders);
    }
}
