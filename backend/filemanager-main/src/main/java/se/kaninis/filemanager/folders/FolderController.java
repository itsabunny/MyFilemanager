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
     * Skapar en ny mapp kopplad till den inloggade användaren.
     *
     * @param name Mappens namn.
     * @param authUser Den inloggade användaren.
     * @return ResponseEntity med den skapade mappen och en HATEOAS-länk.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestParam String name,
                                          @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att skapa en mapp.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        FolderEntity folder = folderService.createFolder(name, userOpt.get());
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FolderController.class)
                .getFolder(folder.getId(), authUser)).withSelfRel();

        return ResponseEntity.ok().body(folder).header("Link", selfLink.toUri().toString());
    }

    /**
     * Hämtar alla mappar kopplade till den inloggade användaren.
     *
     * @param authUser Den inloggade användaren.
     * @return ResponseEntity med lista över mappar och HATEOAS-länkar.
     */
    @GetMapping
    public ResponseEntity<?> getAllFolders(@AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att se dina mappar.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        List<FolderEntity> folders = folderService.getAllFolders(userOpt.get());
        List<Link> links = folders.stream()
                .map(folder -> WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FolderController.class)
                        .getFolder(folder.getId(), authUser)).withSelfRel())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(folders).header("Link", links.toString());
    }

    /**
     * Hämtar en specifik mapp.
     *
     * @param id Mappens ID.
     * @param authUser Den inloggade användaren.
     * @return ResponseEntity med mappen om den hittas.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFolder(@PathVariable Long id, @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att hämta en mapp.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        Optional<FolderEntity> folderOpt = folderService.getFolderById(id, userOpt.get());
        return folderOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body("Mapp ej hittad."));
    }

    /**
     * Raderar en mapp om den tillhör användaren.
     *
     * @param id Mappens ID.
     * @param authUser Den inloggade användaren.
     * @return ResponseEntity med statusmeddelande.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long id, @AuthenticationPrincipal OAuth2User authUser) {
        if (authUser == null) {
            return ResponseEntity.status(401).body("Du måste vara inloggad för att radera mappar.");
        }

        String username = authUser.getAttribute("name");
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Användare ej hittad.");
        }

        boolean deleted = folderService.deleteFolder(id, userOpt.get());
        if (deleted) {
            return ResponseEntity.ok("Mappen har raderats.");
        } else {
            return ResponseEntity.status(404).body("Mappen kunde inte hittas eller tillhör inte dig.");
        }
    }
}
