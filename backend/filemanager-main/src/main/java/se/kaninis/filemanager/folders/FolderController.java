package se.kaninis.filemanager.folders;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import se.kaninis.filemanager.users.UserEntity;
import se.kaninis.filemanager.users.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller för hantering av mappar i systemet.
 */
@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;

    public FolderController(FolderService folderService, UserService userService) {
        this.folderService = folderService;
        this.userService = userService;
    }

    /**
     * Hämtar alla mappar kopplade till den inloggade användaren.
     *
     * @param authUser Den inloggade användaren.
     * @return ResponseEntity med lista över mappar och HATEOAS-länkar.
     */
    @GetMapping
    public ResponseEntity<List<FolderEntity>> getAllFolders(@AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).build();
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        List<FolderEntity> folders = folderService.getAllFolders(userOpt.get());
        return ResponseEntity.ok(folders);
    }
}
