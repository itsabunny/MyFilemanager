package se.kaninis.filemanager.files;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.sql.Blob;
import java.util.Optional;

public interface FileRepository extends CrudRepository<FileEntity, Long> {

    // Hämtar endast metadata (snabbare än att hämta hela filinnehållet)
    @Query("SELECT f.id, f.name, f.folder FROM FileEntity f WHERE f.id = :id")
    Optional<FileEntity> findMetadataById(@Param("id") Long id);

    // Hämtar endast filinnehåll
    @Query("SELECT f.content FROM FileEntity f WHERE f.id = :id")
    Optional<Blob> findFileContentById(@Param("id") Long id);

    // Kontrollera om en fil finns utan att ladda in den i minnet
    @Query("SELECT COUNT(f) > 0 FROM FileEntity f WHERE f.id = :id")
    boolean existsById(@Param("id") Long id);
}
