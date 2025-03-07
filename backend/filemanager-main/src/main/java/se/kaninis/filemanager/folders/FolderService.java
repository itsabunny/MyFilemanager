package se.kaninis.filemanager.folders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    /**
     * Skapar en ny mapp kopplad till den inloggade användaren.
     * @param name Mappnamn
     * @param user Inloggad användare
     * @return Den skapade mappen
     */
    public FolderEntity createFolder(String name, UserEntity user) {
        FolderEntity folder = new FolderEntity();
        folder.setName(name);
        folder.setOwner(user);
        return folderRepository.save(folder);
    }

    /**
     * Hämtar alla mappar för en specifik användare.
     * @param user Inloggad användare
     * @return Lista över användarens mappar
     */
    public Stream<FolderEntity> getAllFolders(UserEntity user) {
        return folderRepository.findAll().stream()
                .filter(folder -> folder.getOwner().equals(user));
    }
}
