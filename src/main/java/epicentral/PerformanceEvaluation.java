package epicentral;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "kpi_evaluations_erp")
public class PerformanceEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El empleado que está siendo evaluado
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User employee;

    private LocalDate evaluationDate; // Fecha de la evaluación
    private String evaluatorName; // Quién realizó la evaluación (Ej: admin)

    // Calificaciones del 1 al 5
    private Integer technicalScore;
    private Integer punctualityScore;
    private Integer safetyScore; // Muy importante para obras de GLP
    private Integer efficiencyScore;    // 1-5 (Basado en cumplimiento de tiempos)
    private Integer clientSatisfaction; // 1-5 (Feedback de clientes en obras GLP/TICs)
    private Integer colleagueScore;    // 1-5 (Satisfacción de compañeros/trabajo en equipo)

    @Column(columnDefinition = "TEXT")
    private String feedback; // Comentarios de retroalimentación

    public PerformanceEvaluation() {}

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getEmployee() { return employee; }
    public void setEmployee(User employee) { this.employee = employee; }

    public LocalDate getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }

    public String getEvaluatorName() { return evaluatorName; }
    public void setEvaluatorName(String evaluatorName) { this.evaluatorName = evaluatorName; }

    public Integer getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(Integer technicalScore) { this.technicalScore = technicalScore; }

    public Integer getPunctualityScore() { return punctualityScore; }
    public void setPunctualityScore(Integer punctualityScore) { this.punctualityScore = punctualityScore; }

    public Integer getSafetyScore() { return safetyScore; }
    public void setSafetyScore(Integer safetyScore) { this.safetyScore = safetyScore; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Integer getEfficiencyScore() { return efficiencyScore; }
    public void setEfficiencyScore(Integer efficiencyScore) { this.efficiencyScore = efficiencyScore; }

    public Integer getClientSatisfaction() { return clientSatisfaction; }
    public void setClientSatisfaction(Integer clientSatisfaction) { this.clientSatisfaction = clientSatisfaction; }

    public Integer getColleagueScore() { return colleagueScore; }
    public void setColleagueScore(Integer colleagueScore) { this.colleagueScore = colleagueScore; }

    // Metodo auxiliar para calcular el promedio en la vista
    public Double getAverageScore() {
        return (technicalScore + punctualityScore + safetyScore +
                efficiencyScore + clientSatisfaction + colleagueScore) / 6.0;
    }
}