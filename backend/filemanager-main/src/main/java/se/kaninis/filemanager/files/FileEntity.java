package se.kaninis.filemanager.files;

import jakarta.persistence.*;
import lombok.Data;
import se.kaninis.filemanager.folders.FolderEntity;
import se.kaninis.filemanager.users.UserEntity;

import java.sql.Blob;

/**
 * Entity-klass som representerar en fil i systemet.
 */
@Entity
@Data
public class FileEntity {

    /**
     * Unikt ID för filen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Filens namn.
     */
    private String name;

    /**
     * Filens innehåll lagrat som en Blob.
     */
    @Lob
    @Column(columnDefinition = "BYTEA")
    private Blob content;

    /**
     * Mappen som filen tillhör (kan vara null om den ligger på root-nivå).
     */
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderEntity folder;

    /**
     * Användaren som äger filen.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity owner;
}
