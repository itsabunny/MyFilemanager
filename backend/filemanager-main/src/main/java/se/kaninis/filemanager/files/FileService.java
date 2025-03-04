package se.kaninis.filemanager.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kaninis.filemanager.users.UserEntity;
import se.kaninis.filemanager.users.UserRepository;

import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Sparar en fil om användaren äger mappen den laddas upp i.
     * @param file Filobjekt att spara
     * @param user Inloggad användare
     * @return Sparad fil eller null om användaren inte äger mappen
     */
    public FileEntity saveFile(FileEntity file, UserEntity user) {
        if (file.getFolder().getOwner().equals(user)) {
            return fileRepository.save(file);
        }
        return null;
    }

    /**
     * Hämtar en fil om användaren äger den.
     * @param id Fil-ID
     * @param user Inloggad användare
     * @return Fil om användaren äger den, annars tomt resultat
     */
    public Optional<FileEntity> getFileById(Long id, UserEntity user) {
        return fileRepository.findById(id)
                .filter(file -> file.getFolder().getOwner().equals(user));
    }

    /**
     * Tar bort en fil om användaren äger den.
     * @param id Fil-ID
     * @param user Inloggad användare
     * @return True om filen raderades, annars false
     */
    public boolean deleteFile(Long id, UserEntity user) {
        return fileRepository.findById(id)
                .filter(file -> file.getFolder().getOwner().equals(user))
                .map(file -> {
                    fileRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }
}
