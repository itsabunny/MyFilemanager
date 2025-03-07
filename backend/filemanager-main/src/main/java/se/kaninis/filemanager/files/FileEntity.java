package se.kaninis.filemanager.files;

import jakarta.persistence.*;
import lombok.Data;
import se.kaninis.filemanager.folders.FolderEntity;
import se.kaninis.filemanager.users.UserEntity;

import java.sql.Blob;

@Entity
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private Blob content;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderEntity folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity owner; // Ny kolumn för att koppla filen till användaren!
}
