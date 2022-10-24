package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Gloves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlovesRepository extends JpaRepository<Gloves, Long> {

    // ----- show all -----
    List<Gloves> findAllByOrderById();

    // ----- search -----
    List<Gloves> findAllByNameContainingIgnoreCase(String partOfName);

    // ----- edit booking info / assign equipment to riders -----
    List<Gloves> findAllByOrderBySize();
}
