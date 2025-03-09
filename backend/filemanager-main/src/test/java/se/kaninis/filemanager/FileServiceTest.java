package se.kaninis.filemanager.files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Enhetstester för FileService.
 */
@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    /**
     * Testar att en fil sparas korrekt.
     */
    @Test
    void testSaveFile() throws SQLException {
        byte[] content = new byte[]{1, 2, 3};
        when(fileRepository.save(any())).thenReturn(new FileEntity());

        assertDoesNotThrow(() -> fileService.saveFile("testfile.txt", content));
        verify(fileRepository, times(1)).save(any());
    }
}
