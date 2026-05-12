package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    // Metodo para filtrar solo por fechas
    List<InventoryMovement> findByMovementDateBetween(LocalDateTime start, LocalDateTime end);

    // Metodo para filtrar por tipo (Entrada/Salida) y fechas
    List<InventoryMovement> findByMovementTypeAndMovementDateBetween(String type, LocalDateTime start, LocalDateTime end);

    // Metodo para traer todo el historial
    List<InventoryMovement> findAllByOrderByMovementDateDesc();

    List<InventoryMovement> findByCostCenterIdOrderByMovementDateDesc(Long costCenterId);
}