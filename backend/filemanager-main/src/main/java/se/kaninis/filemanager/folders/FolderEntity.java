package se.kaninis.filemanager.folders;

import jakarta.persistence.*;
import lombok.Data;
import se.kaninis.filemanager.files.FileEntity;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;

/**
 * Entity-klass som representerar en mapp i systemet.
 */
@Entity
@Data
public class FolderEntity {

    /**
     * Unikt ID för mappen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mappens namn.
     */
    private String name;

    /**
     * Lista över filer som finns i mappen.
     */
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files;

    /**
     * Användaren som äger mappen.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity owner;

    /**
     * Överordnad mapp (om denna mapp ligger i en annan mapp).
     */
    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private FolderEntity parentFolder;

    /**
     * Lista över undermappar.
     */
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolderEntity> subFolders;
}
