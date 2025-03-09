package se.kaninis.filemanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrationstester för att verifiera att applikationen startar och att API:et fungerar.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilemanagerApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Verifierar att applikationen startar utan fel.
     */
    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    /**
     * Testar att hämtning av API-status fungerar.
     */
    @Test
    void testMonitoringStatus() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/monitoring/status", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("status", "running");
    }
}
