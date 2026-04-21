package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    // Listar nóminas de la más reciente a la más antigua
    List<Payroll> findAllByOrderByIssueDateDesc();
}