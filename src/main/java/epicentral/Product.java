package epicentral;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_products_erp")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Codigo interno, Ej: TIC-001, GLP-045

    @Column(nullable = false)
    private String name; // Ej: Cable UTP Cat 6, Válvula de Alta Presión

    @Column(nullable = false)
    private String category; // GLP, TICs, Herramientas, EPP

    @Column(nullable = false)
    private String unit; // Unidad, Metro, Rollo, Par

    private Double currentStock = 0.0;
    private Double minimumStock = 0.0; // Para alertas de escasez
    private Double unitCost = 0.0; // Costo unitario de compra

    public Product() {}

    public Product(String code, String name, String category, String unit, Double currentStock, Double minimumStock) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getCurrentStock() { return currentStock; }
    public void setCurrentStock(Double currentStock) { this.currentStock = currentStock; }

    public Double getMinimumStock() { return minimumStock; }
    public void setMinimumStock(Double minimumStock) { this.minimumStock = minimumStock; }

    public Double getUnitCost() { return unitCost; }
    public void setUnitCost(Double unitCost) { this.unitCost = unitCost; }
}