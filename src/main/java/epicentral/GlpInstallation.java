package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "glp_installations_erp")
public class GlpInstallation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Conectamos la instalación con un Cliente del módulo contable
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String projectName; // Ej: Tren de Regulación Hornos, Matriz de Secado

    @Column(nullable = false)
    private String location; // Ej: Galpón 3, Planta Baja

    private Double tankCapacityGallons;

    private String regulatorType; // Ej: Alta Presión, Regulación 2 Etapas

    private Boolean hasFireSystem = false; // Sistema contra incendios

    private LocalDate installationDate;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate; // Vital para lanzar alertas preventivas

    private String status = "OPERATIVO"; // OPERATIVO, REQUIERE_INSPECCION, FUERA_DE_SERVICIO

    public GlpInstallation() {}

    public GlpInstallation(Client client, String projectName, String location, Double tankCapacityGallons, String regulatorType, Boolean hasFireSystem, LocalDate installationDate, LocalDate lastMaintenanceDate, LocalDate nextMaintenanceDate) {
        this.client = client;
        this.projectName = projectName;
        this.location = location;
        this.tankCapacityGallons = tankCapacityGallons;
        this.regulatorType = regulatorType;
        this.hasFireSystem = hasFireSystem;
        this.installationDate = installationDate;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getTankCapacityGallons() { return tankCapacityGallons; }
    public void setTankCapacityGallons(Double tankCapacityGallons) { this.tankCapacityGallons = tankCapacityGallons; }

    public String getRegulatorType() { return regulatorType; }
    public void setRegulatorType(String regulatorType) { this.regulatorType = regulatorType; }

    public Boolean getHasFireSystem() { return hasFireSystem; }
    public void setHasFireSystem(Boolean hasFireSystem) { this.hasFireSystem = hasFireSystem; }

    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }

    public LocalDate getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }

    public LocalDate getNextMaintenanceDate() { return nextMaintenanceDate; }
    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}