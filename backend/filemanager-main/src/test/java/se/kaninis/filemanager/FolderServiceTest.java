package se.kaninis.filemanager.folders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.kaninis.filemanager.users.UserEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Enhetstester för FolderService.
 */
@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @InjectMocks
    private FolderService folderService;

    /**
     * Testar att en mapp skapas korrekt.
     */
    @Test
    void testCreateFolder() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        when(folderRepository.save(any())).thenReturn(new FolderEntity());

        assertDoesNotThrow(() -> folderService.createFolder("Test Folder", user));
        verify(folderRepository, times(1)).save(any());
    }
}
