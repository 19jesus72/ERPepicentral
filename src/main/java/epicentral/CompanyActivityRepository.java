package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyActivityRepository extends JpaRepository<CompanyActivity, Long> {
}
