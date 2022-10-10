package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.KneeProtection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KneeProtectionRepository extends JpaRepository<KneeProtection, Long> {

    // ----- show all -----
    List<KneeProtection> findAllByOrderById();

    // ----- search -----
    List<KneeProtection> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<KneeProtection> findAllByOrderBySize();
}
