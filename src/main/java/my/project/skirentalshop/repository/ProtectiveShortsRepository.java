package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.ProtectiveShorts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProtectiveShortsRepository extends JpaRepository<ProtectiveShorts, Long> {

    // ----- show all -----
    List<ProtectiveShorts> findAllByOrderById();

    // ----- search -----
    List<ProtectiveShorts> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<ProtectiveShorts> findAllByOrderBySize();
}
