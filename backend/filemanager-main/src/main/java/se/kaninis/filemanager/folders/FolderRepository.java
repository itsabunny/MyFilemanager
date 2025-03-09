package se.kaninis.filemanager.folders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;

/**
 * Repository för hantering av mappar i databasen.
 */
public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    /**
     * Hämtar alla mappar som tillhör en viss användare.
     *
     * @param owner Användaren som äger mapparna.
     * @return Lista med mappar som tillhör användaren.
     */
    List<FolderEntity> findByOwner(UserEntity owner);

    /**
     * Hämtar alla undermappar till en given mapp.
     *
     * @param parentFolder Den överordnade mappen.
     * @return Lista över undermappar.
     */
    List<FolderEntity> findByParentFolder(FolderEntity parentFolder);

    /**
     * Kontrollerar om en viss användare äger en specifik mapp.
     *
     * @param folderId Mappens ID.
     * @param owner Användaren som ska verifieras.
     * @return true om användaren äger mappen, annars false.
     */
    @Query("SELECT COUNT(f) > 0 FROM FolderEntity f WHERE f.id = :folderId AND f.owner = :owner")
    boolean isOwner(@Param("folderId") Long folderId, @Param("owner") UserEntity owner);
}
