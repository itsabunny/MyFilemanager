package se.kaninis.filemanager.files;

import jakarta.persistence.*;
import lombok.Data;
import se.kaninis.filemanager.folders.FolderEntity;

@Entity
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderEntity folder;
}
