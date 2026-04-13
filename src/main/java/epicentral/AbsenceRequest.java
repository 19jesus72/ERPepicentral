package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "absences_erp")
public class AbsenceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A quién pertenece el permiso
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String absenceType; // Ej: "Enfermedad", "Vacaciones", "Calamidad Doméstica", "Permiso Personal"

    @Column(nullable = false)
    private LocalDate startDate; // Fecha de inicio de la ausencia

    @Column(nullable = false)
    private LocalDate endDate; // Fecha de fin de la ausencia

    @Column(columnDefinition = "TEXT")
    private String reason; // Justificación detallada

    private String status = "PENDIENTE"; // PENDIENTE, APROBADO, RECHAZADO

    @Column(columnDefinition = "TEXT")
    private String adminNotes; // Notas de Recursos Humanos (Ej: "Presentó certificado médico")

    public AbsenceRequest() {}

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAbsenceType() { return absenceType; }
    public void setAbsenceType(String absenceType) { this.absenceType = absenceType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }
}