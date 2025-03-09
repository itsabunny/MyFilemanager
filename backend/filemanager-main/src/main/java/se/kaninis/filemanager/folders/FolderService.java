package se.kaninis.filemanager.folders;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.kaninis.filemanager.users.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviceklass för hantering av mappar.
 */
@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    /**
     * Skapar en ny mapp kopplad till den inloggade användaren.
     *
     * @param name Mappens namn.
     * @param user Inloggad användare som äger mappen.
     * @return Den skapade mappen.
     */
    @Transactional
    public FolderEntity createFolder(String name, UserEntity user) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Mappnamn får inte vara tomt!");
        }
        if (user == null) {
            throw new IllegalArgumentException("Användare får inte vara null!");
        }

        FolderEntity folder = new FolderEntity();
        folder.setName(name);
        folder.setOwner(user);
        return folderRepository.save(folder);
    }

    /**
     * Hämtar alla mappar som tillhör en specifik användare.
     *
     * @param user Inloggad användare.
     * @return Lista över användarens mappar.
     */
    public List<FolderEntity> getAllFolders(UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("Användare får inte vara null!");
        }
        return folderRepository.findAll().stream()
                .filter(folder -> folder.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    /**
     * Hämtar en specifik mapp om den tillhör användaren.
     *
     * @param id   Mappens ID.
     * @param user Inloggad användare.
     * @return Optional med mappen om den finns.
     */
    public Optional<FolderEntity> getFolderById(Long id, UserEntity user) {
        return folderRepository.findById(id)
                .filter(folder -> folder.getOwner().equals(user));
    }

    /**
     * Raderar en mapp om den tillhör användaren.
     *
     * @param id   Mappens ID.
     * @param user Inloggad användare.
     * @return true om mappen raderades, annars false.
     */
    @Transactional
    public boolean deleteFolder(Long id, UserEntity user) {
        Optional<FolderEntity> folderOpt = getFolderById(id, user);
        if (folderOpt.isPresent()) {
            folderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
