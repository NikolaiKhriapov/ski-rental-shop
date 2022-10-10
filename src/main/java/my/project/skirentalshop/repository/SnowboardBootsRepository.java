package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.SnowboardBoots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnowboardBootsRepository extends JpaRepository<SnowboardBoots, Long> {

    // ----- show all -----
    List<SnowboardBoots> findAllByOrderById();

    // ----- search -----
    List<SnowboardBoots> findAllByNameContainingIgnoreCase(String search);

    //// ----- edit booking info / assign equipment to riders -----
    List<SnowboardBoots> findAllByOrderBySize();
}