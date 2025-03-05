package se.kaninis.filemanager.files;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.sql.Blob;
import java.util.Optional;

public interface FileRepository extends CrudRepository<FileEntity, Long> {

    // Hämta metadata (snabbare än att hämta hela filinnehållet)
    @Query("SELECT f FROM FileEntity f WHERE f.id = :id")
    Optional<FileEntity> findMetadataById(@Param("id") Long id);

    // Hämta endast filinnehåll
    @Query("SELECT f.content FROM FileEntity f WHERE f.id = :id")
    Optional<Blob> findFileContentById(@Param("id") Long id);
}
