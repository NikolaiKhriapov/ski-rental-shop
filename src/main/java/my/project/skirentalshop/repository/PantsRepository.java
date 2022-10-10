package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Pants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PantsRepository extends JpaRepository<Pants, Long> {

    // ----- show all -----
    List<Pants> findAllByOrderById();

    // ----- search -----
    List<Pants> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<Pants> findAllByOrderBySize();
}
