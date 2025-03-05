package se.kaninis.filemanager.files;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.kaninis.filemanager.users.UserEntity;
import se.kaninis.filemanager.users.UserRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FileEntity saveFile(String name, byte[] content, Long folderId, UserEntity user) throws SQLException {
        Blob blob = new SerialBlob(content);  // Konverterar byte[] till Blob

        FileEntity file = new FileEntity();
        file.setName(name);
        file.setContent(blob);

        fileRepository.save(file);
        return file;
    }

    public byte[] getFileContent(Long id, UserEntity user) throws SQLException {
        Optional<Blob> blobOpt = fileRepository.findFileContentById(id);
        if (blobOpt.isEmpty()) {
            return null;
        }

        Blob blob = blobOpt.get();
        return blob.getBytes(1, (int) blob.length());  // Konverterar Blob till byte[]
    }

    public boolean deleteFile(Long id, UserEntity user) {
        return fileRepository.findById(id)
                .filter(file -> file.getFolder().getOwner().equals(user))
                .map(file -> {
                    fileRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }
}
