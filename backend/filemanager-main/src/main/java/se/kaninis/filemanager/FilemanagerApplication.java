package se.kaninis.filemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Huvudklassen för FileManager-applikationen.
 *
 * Startar Spring Boot-applikationen och initialiserar nödvändiga konfigurationer.
 */
@SpringBootApplication
public class FilemanagerApplication {

    private static final Logger logger = LoggerFactory.getLogger(FilemanagerApplication.class);

    public static void main(String[] args) {
        logger.info("Starting FileManager application...");
        SpringApplication.run(FilemanagerApplication.class, args);
        logger.info("FileManager application started successfully.");
    }
}
