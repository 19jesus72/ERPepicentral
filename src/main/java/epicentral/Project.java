package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects_erp")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ej: "Instalación Red GLP Granja Palestina"

    @Column(columnDefinition = "TEXT")
    private String description;

    // El cliente dueño de la obra
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // El ingeniero o técnico a cargo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate expectedEndDate;

    private Double totalBudget = 0.0;

    private String status = "PLANIFICACION"; // PLANIFICACION, EJECUCION, REVISION, ENTREGADO

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTask> tasks = new ArrayList<>();

    public Project() {}

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(LocalDate expectedEndDate) { this.expectedEndDate = expectedEndDate; }
    public Double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<ProjectTask> getTasks() { return tasks; }
    public void setTasks(List<ProjectTask> tasks) { this.tasks = tasks; }
    public CostCenter getCostCenter() { return costCenter; }
    public void setCostCenter(CostCenter costCenter) { this.costCenter = costCenter; }
}