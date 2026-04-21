package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "accounting_invoices_erp")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber; // Secuencial, Ej: 001-001-000001

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String projectDescription; // Ej: Mantenimiento Redes Granja

    @Column(nullable = false)
    private LocalDate issueDate; // Fecha de Emisión

    private LocalDate dueDate; // Fecha de Vencimiento (Límite de pago)

    private Double subtotal = 0.0;
    private Double tax = 0.0; // IVA (Ej: 15% en Ecuador actualmente)
    private Double total = 0.0;

    private String status = "PENDIENTE"; // PENDIENTE, PAGADA, ANULADA

    public Invoice() {}

    // Método de negocio para calcular el total
    public void calculateTotal(Double ivaPercentage) {
        if (this.subtotal == null) this.subtotal = 0.0;
        this.tax = this.subtotal * (ivaPercentage / 100.0);
        this.total = this.subtotal + this.tax;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getTax() { return tax; }
    public void setTax(Double tax) { this.tax = tax; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
