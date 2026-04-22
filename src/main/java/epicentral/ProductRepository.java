package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Buscar productos ordenados por categoría
    List<Product> findAllByOrderByCategoryAscNameAsc();
}