package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounting_journal_entries_erp")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate entryDate; // Fecha de la transacción

    @Column(nullable = false)
    private String description; // Glosa / Concepto del asiento

    private String referenceDocument; // Ej: "Factura #001-002", "Transferencia #4432"

    private String status = "REGISTRADO"; // REGISTRADO, ANULADO

    // Relación: Un asiento tiene varias líneas (Debe y Haber)
    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalLine> lines = new ArrayList<>();

    public JournalEntry() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReferenceDocument() { return referenceDocument; }
    public void setReferenceDocument(String referenceDocument) { this.referenceDocument = referenceDocument; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<JournalLine> getLines() { return lines; }
    public void setLines(List<JournalLine> lines) { this.lines = lines; }

    // Metodo auxiliar para agregar líneas fácilmente
    public void addLine(JournalLine line) {
        lines.add(line);
        line.setJournalEntry(this);
    }
    // Importa LocalDateTime y User
    private java.time.LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    // ... No olvides añadir sus Getters y Setters ...
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
