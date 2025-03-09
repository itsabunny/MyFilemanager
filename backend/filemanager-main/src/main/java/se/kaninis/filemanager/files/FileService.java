package se.kaninis.filemanager.files;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.rowset.serial.SerialBlob;

/**
 * Serviceklass för hantering av filer.
 */
@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Sparar en fil i databasen.
     *
     * @param name    Filens namn.
     * @param content Filens innehåll i byte-array.
     * @return ID för den sparade filen.
     * @throws SQLException Om ett databasfel uppstår.
     */
    @Transactional
    public Long saveFile(String name, byte[] content) throws SQLException {
        if (content.length > 10 * 1024 * 1024) { // Max 10MB
            throw new IllegalArgumentException("Filstorleken får inte överstiga 10MB.");
        }

        Blob blob = new SerialBlob(content);  // Konverterar byte[] till Blob
        FileEntity file = new FileEntity();
        file.setName(name);
        file.setContent(blob);
        fileRepository.save(file);

        return file.getId(); // Returnerar filens ID
    }

    /**
     * Hämtar innehållet för en fil.
     *
     * @param id Filens ID.
     * @return En Optional med filens innehåll som byte-array om den finns.
     * @throws SQLException Om ett databasfel uppstår.
     */
    public Optional<byte[]> getFileContent(Long id) throws SQLException {
        Optional<Blob> blobOpt = fileRepository.findFileContentById(id);
        if (blobOpt.isPresent()) {
            Blob blob = blobOpt.get();
            return Optional.of(blob.getBytes(1, (int) blob.length()));  // Konverterar Blob till byte[]
        }
        return Optional.empty();
    }

    /**
     * Raderar en fil om den existerar.
     *
     * @param id Filens ID.
     * @return true om filen raderades, annars false.
     */
    @Transactional
    public boolean deleteFile(Long id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
