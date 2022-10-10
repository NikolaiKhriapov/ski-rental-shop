package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.SkiBoots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiBootsRepository extends JpaRepository<SkiBoots, Long> {

    // ----- show all -----
    List<SkiBoots> findAllByOrderById();

    // ----- search -----
    List<SkiBoots> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<SkiBoots> findAllByOrderBySize();
}
