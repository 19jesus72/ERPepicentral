package epicentral;

import jakarta.persistence.*;

@Entity
@Table(name = "accounting_clients_erp")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rucOrDni; // RUC o Cédula

    @Column(nullable = false)
    private String businessName; // Razón Social o Nombre

    private String contactEmail;
    private String phone;
    private String address;

    public Client() {}

    public Client(String rucOrDni, String businessName, String contactEmail, String phone, String address) {
        this.rucOrDni = rucOrDni;
        this.businessName = businessName;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.address = address;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRucOrDni() { return rucOrDni; }
    public void setRucOrDni(String rucOrDni) { this.rucOrDni = rucOrDni; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
