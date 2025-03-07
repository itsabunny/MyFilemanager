package se.kaninis.filemanager.folders;

import org.springframework.stereotype.Service;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;
import java.util.stream.Stream;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    // ✅ Bytt från @Autowired till konstruktionsinjektion
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    /**
     * Skapar en ny mapp kopplad till den inloggade användaren.
     * @param name Mappnamn
     * @param user Inloggad användare
     * @return Den skapade mappen
     */
    public FolderEntity createFolder(String name, UserEntity user) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Mappnamn får inte vara tomt!"); // Felhantering
        }
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
        if (user == null) {
            throw new IllegalArgumentException("Användare får inte vara null!"); // Felhantering
        }
        return folderRepository.findAll().stream()
                .filter(folder -> folder.getOwner().equals(user));
    }
}
