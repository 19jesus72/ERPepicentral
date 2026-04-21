package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payroll_erp")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private String period; // Ej: "Abril 2026"

    private LocalDate issueDate;

    // Ingresos
    private Double baseSalary;
    private Double overtimeBonus;
    private Double performanceBonus; // Recompensa por KPI alto

    // Egresos
    private Double iessDeduction; // 9.45% por defecto
    private Double absenceDeduction; // Descuentos por faltas

    private Double netSalary; // Total a transferir al colaborador

    private String status = "PENDIENTE"; // PENDIENTE, PAGADO

    public Payroll() {}

    // Método de negocio para calcular la nómina
    public void calculateNetSalary() {
        if (this.baseSalary == null) this.baseSalary = 0.0;
        if (this.overtimeBonus == null) this.overtimeBonus = 0.0;
        if (this.performanceBonus == null) this.performanceBonus = 0.0;
        if (this.absenceDeduction == null) this.absenceDeduction = 0.0;

        // Cálculo del IESS (9.45% sobre el sueldo base y horas extras)
        this.iessDeduction = (this.baseSalary + this.overtimeBonus) * 0.0945;

        double totalIngresos = this.baseSalary + this.overtimeBonus + this.performanceBonus;
        double totalEgresos = this.iessDeduction + this.absenceDeduction;

        this.netSalary = totalIngresos - totalEgresos;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getEmployee() { return employee; }
    public void setEmployee(User employee) { this.employee = employee; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }

    public Double getOvertimeBonus() { return overtimeBonus; }
    public void setOvertimeBonus(Double overtimeBonus) { this.overtimeBonus = overtimeBonus; }

    public Double getPerformanceBonus() { return performanceBonus; }
    public void setPerformanceBonus(Double performanceBonus) { this.performanceBonus = performanceBonus; }

    public Double getIessDeduction() { return iessDeduction; }
    public void setIessDeduction(Double iessDeduction) { this.iessDeduction = iessDeduction; }

    public Double getAbsenceDeduction() { return absenceDeduction; }
    public void setAbsenceDeduction(Double absenceDeduction) { this.absenceDeduction = absenceDeduction; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
