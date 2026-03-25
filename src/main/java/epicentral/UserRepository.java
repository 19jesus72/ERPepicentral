package epicentral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring "entiende" este metodo por el nombre y buscará por la columna email
    Optional<User> findByEmail(String email);

    // También podrías buscar por el primer apellido si fuera necesario para el ERP
    List<User> findByFirstSurname(String firstSurname);
}
