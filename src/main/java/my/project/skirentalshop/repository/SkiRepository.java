package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Ski;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiRepository extends JpaRepository<Ski, Long> {

    // ----- show all -----
    List<Ski> findAllByOrderById();

    // ----- search -----
    List<Ski> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<Ski> findAllByOrderBySize();
}
