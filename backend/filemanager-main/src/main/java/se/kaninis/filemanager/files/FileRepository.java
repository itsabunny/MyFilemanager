package se.kaninis.filemanager.files;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.sql.Blob;
import java.util.Optional;

/**
 * Repository för hantering av filer i databasen.
 */
public interface FileRepository extends CrudRepository<FileEntity, Long> {

    /**
     * Hämtar endast metadata för en fil utan att ladda dess innehåll.
     *
     * @param id Filens ID.
     * @return Optional med metadata om filen.
     */
    @Query("SELECT f FROM FileEntity f WHERE f.id = :id")
    Optional<FileEntity> findMetadataById(@Param("id") Long id);

    /**
     * Hämtar endast innehållet för en fil utan att ladda dess metadata.
     *
     * @param id Filens ID.
     * @return Optional med filens innehåll som Blob.
     */
    @Query("SELECT f.content FROM FileEntity f WHERE f.id = :id")
    Optional<Blob> findFileContentById(@Param("id") Long id);

    /**
     * Kontrollerar om en fil med det angivna ID:t finns.
     *
     * @param id Filens ID.
     * @return true om filen finns, annars false.
     */
    boolean existsById(Long id);
}
