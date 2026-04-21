package epicentral;

import jakarta.persistence.*;

@Entity
@Table(name = "accounting_journal_lines_erp")
public class JournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A qué asiento pertenece esta línea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    // Qué cuenta contable se está afectando
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Double debit = 0.0;  // DEBE
    private Double credit = 0.0; // HABER

    public JournalLine() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JournalEntry getJournalEntry() { return journalEntry; }
    public void setJournalEntry(JournalEntry journalEntry) { this.journalEntry = journalEntry; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Double getDebit() { return debit; }
    public void setDebit(Double debit) { this.debit = debit != null ? debit : 0.0; }

    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit != null ? credit : 0.0; }
}
