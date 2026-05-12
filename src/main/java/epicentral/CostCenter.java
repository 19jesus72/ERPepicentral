package epicentral;

import jakarta.persistence.*;

@Entity
@Table(name = "cost_centers_erp")
public class CostCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Ej: CC-PALESTINA-001

    @Column(nullable = false)
    private String name; // Ej: Proyecto GLP Granja Palestina

    private Double assignedBudget = 0.0; // Presupuesto asignado

    public CostCenter() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getAssignedBudget() { return assignedBudget; }
    public void setAssignedBudget(Double assignedBudget) { this.assignedBudget = assignedBudget; }
}