package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GlpInstallationRepository extends JpaRepository<GlpInstallation, Long> {
    // Ordenamos para que los mantenimientos próximos o vencidos salgan primero
    List<GlpInstallation> findAllByOrderByNextMaintenanceDateAsc();
}