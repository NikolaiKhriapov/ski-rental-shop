package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Jacket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JacketRepository extends JpaRepository<Jacket, Long> {

    // ----- show all -----
    List<Jacket> findAllByOrderById();

    // ----- search -----
    List<Jacket> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<Jacket> findAllByOrderBySize();
}
