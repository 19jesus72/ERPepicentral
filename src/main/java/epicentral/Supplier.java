package epicentral;

import jakarta.persistence.*;

@Entity
@Table(name = "accounting_suppliers_erp")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = false)
    private String businessName; // Razón Social

    private String contactEmail;
    private String phone;

    @Column(nullable = false)
    private String supplyCategory; // Ej: "Equipos TICs", "Tuberías GLP", "Servicios"

    public Supplier() {}

    public Supplier(String ruc, String businessName, String contactEmail, String phone, String supplyCategory) {
        this.ruc = ruc;
        this.businessName = businessName;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.supplyCategory = supplyCategory;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSupplyCategory() { return supplyCategory; }
    public void setSupplyCategory(String supplyCategory) { this.supplyCategory = supplyCategory; }
}
