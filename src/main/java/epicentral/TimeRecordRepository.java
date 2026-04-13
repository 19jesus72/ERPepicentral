package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    // Buscar si un técnico ya inició su jornada en una fecha específica
    Optional<TimeRecord> findByUserAndWorkDate(User user, LocalDate workDate);

    // Obtenertodo el historial de un técnico ordenado por fecha
    List<TimeRecord> findByUserOrderByWorkDateDesc(User user);

    // CORRECCIÓN: Metodo renombrado para que coincida con "assignedProject"
    List<TimeRecord> findByAssignedProjectContainingIgnoreCase(String project);

    // Obtener todos los registros de la empresa ordenados por fecha descendente
    List<TimeRecord> findAllByOrderByWorkDateDesc();
}