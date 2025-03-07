package se.kaninis.filemanager.files;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.rowset.serial.SerialBlob;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void saveFile(String name, byte[] content) throws SQLException {
        if (content.length > 10 * 1024 * 1024) { // Max 10MB
            throw new IllegalArgumentException("Filstorleken får inte överstiga 10MB.");
        }

        Blob blob = new SerialBlob(content);  // Konverterar byte[] till Blob
        FileEntity file = new FileEntity();
        file.setName(name);
        file.setContent(blob);
        fileRepository.save(file);
    }

    public Optional<byte[]> getFileContent(Long id) throws SQLException {
        Optional<Blob> blobOpt = fileRepository.findFileContentById(id);
        if (blobOpt.isPresent()) {
            Blob blob = blobOpt.get();
            return Optional.of(blob.getBytes(1, (int) blob.length()));  // Konverterar Blob till byte[]
        }

        return Optional.empty();
    }

    public boolean deleteFile(Long id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
