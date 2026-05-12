package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "project_documents_erp")
public class ProjectDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name; // Ej: Planos de Instalación, Acta de Entrega-Recepción

    private LocalDate dueDate;

    private String status = "PENDIENTE"; // PENDIENTE, EN_PROCESO, ENTREGADO

    public ProjectDocument() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}