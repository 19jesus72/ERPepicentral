package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Long> {

    // Traer todas las evaluaciones ordenadas de la más reciente a la más antigua
    List<PerformanceEvaluation> findAllByOrderByEvaluationDateDesc();
}
