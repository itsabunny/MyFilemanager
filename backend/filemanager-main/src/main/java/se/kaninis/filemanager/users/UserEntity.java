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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;  // 🔥 Nytt fält

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<FolderEntity> folders;

    private String role = "USER";  // 🔥 Nytt fält för att hantera roller
}
