package epicentral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.code) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);

    List<Product> findAllByOrderByCategoryAscNameAsc();
}