package se.kaninis.filemanager.users;

import jakarta.persistence.*;
import lombok.Data;
import se.kaninis.filemanager.folders.FolderEntity;

import java.util.List;

@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<FolderEntity> folders;
}
