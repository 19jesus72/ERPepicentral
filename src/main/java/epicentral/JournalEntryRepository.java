package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    // Listar los asientos ordenados por fecha, los más recientes primero
    List<JournalEntry> findAllByOrderByEntryDateDesc();
    List<JournalEntry> findTop100ByOrderByCreatedAtDesc();
}
