package se.kaninis.filemanager.monitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller för att övervaka applikationens status.
 */
@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    /**
     * Hämtar statusinformation om applikationen.
     *
     * @return En Map med applikationens namn, status och serverns aktuella tid.
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("application", "filemanager");
        status.put("status", "running");
        status.put("serverTime", LocalDateTime.now().toString());
        return status;
    }
}
