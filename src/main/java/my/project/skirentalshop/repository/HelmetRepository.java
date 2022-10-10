package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Helmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelmetRepository extends JpaRepository<Helmet, Long> {

    // ----- show all -----
    List<Helmet> findAllByOrderById();

    // ----- search -----
    List<Helmet> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<Helmet> findAllByOrderBySize();
}
