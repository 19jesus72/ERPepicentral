package epicentral;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "logs_erp")
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timestamp;
    private String email;  // Quién realizó la acción
    private String action; // Ej: CREACION, ELIMINACION
    private String details;

    public LogEntry() {}

    public LogEntry(String email, String action, String details) {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.email = email;
        this.action = action;
        this.details = details;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getTimestamp() { return timestamp; }
    public String getEmail() { return email; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
}