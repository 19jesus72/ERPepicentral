package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_records_erp")
public class TimeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate workDate; // Fecha del fichaje

    private LocalTime clockInTime; // Hora de entrada
    private LocalTime clockOutTime; // Hora de salida

    private LocalTime pauseStartTime; // Inicio de almuerzo/pausa
    private LocalTime pauseEndTime; // Fin de almuerzo/pausa

    private String status; // Ej: "TRABAJANDO", "EN PAUSA", "FINALIZADO"

    private String locationMode; // Ej: "Oficina", "Campo"

    // CORRECCIÓN: Nombre sin la palabra reservada "Or"
    private String assignedProject;

    @Column(columnDefinition = "TEXT")
    private String notes; // Comentarios

    public TimeRecord() {}

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }

    public LocalTime getClockInTime() { return clockInTime; }
    public void setClockInTime(LocalTime clockInTime) { this.clockInTime = clockInTime; }

    public LocalTime getClockOutTime() { return clockOutTime; }
    public void setClockOutTime(LocalTime clockOutTime) { this.clockOutTime = clockOutTime; }

    public LocalTime getPauseStartTime() { return pauseStartTime; }
    public void setPauseStartTime(LocalTime pauseStartTime) { this.pauseStartTime = pauseStartTime; }

    public LocalTime getPauseEndTime() { return pauseEndTime; }
    public void setPauseEndTime(LocalTime pauseEndTime) { this.pauseEndTime = pauseEndTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocationMode() { return locationMode; }
    public void setLocationMode(String locationMode) { this.locationMode = locationMode; }

    public String getAssignedProject() { return assignedProject; }
    public void setAssignedProject(String assignedProject) { this.assignedProject = assignedProject; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}