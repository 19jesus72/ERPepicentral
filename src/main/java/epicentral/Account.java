package epicentral;

import jakarta.persistence.*;

@Entity
@Table(name = "accounting_accounts_erp")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Ej: "1.1.1" para Caja, "4.1.1" para Ingresos

    @Column(nullable = false)
    private String name; // Ej: "Banco Pichincha", "Ingresos por Proyectos GLP"

    @Column(nullable = false)
    private String type; // ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO

    private String description;

    // Constructor vacío requerido por Hibernate
    public Account() {}

    // Constructor para inicialización rápida
    public Account(String code, String name, String type, String description) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}