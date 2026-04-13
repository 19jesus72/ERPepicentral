package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {

    // Obtener todas las solicitudes ordenadas de la más reciente a la más antigua
    List<AbsenceRequest> findAllByOrderByStartDateDesc();

    // Buscar permisos pendientes de revisión
    List<AbsenceRequest> findByStatusOrderByStartDateDesc(String status);
}
