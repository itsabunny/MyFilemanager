package se.kaninis.filemanager.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import se.kaninis.filemanager.folders.FolderEntity;

import java.util.List;

/**
 * Entity-klass som representerar en användare i systemet.
 */
@Entity
@Data
public class UserEntity {

    /**
     * Unikt ID för användaren.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Användarnamn, måste vara unikt och får inte vara tomt.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Användarnamn får inte vara tomt")
    private String username;

    /**
     * E-postadress, måste vara unik och i korrekt format.
     */
    @Column(unique = true, nullable = false)
    @Email(message = "Ogiltig e-postadress")
    @NotBlank(message = "E-post får inte vara tomt")
    private String email;

    /**
     * Lista över mappar som ägs av användaren.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolderEntity> folders;

    /**
     * Användarroll, som används för att hantera behörigheter.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
}
