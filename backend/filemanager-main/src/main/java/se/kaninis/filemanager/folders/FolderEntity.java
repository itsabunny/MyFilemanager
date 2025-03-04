package se.kaninis.filemanager.folders;

import jakarta.persistence.*;
import lombok.Data;
import se.kaninis.filemanager.files.FileEntity;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;

@Entity
@Data
public class FolderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FileEntity> files;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity owner;
}
