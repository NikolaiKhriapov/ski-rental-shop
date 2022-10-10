package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Snowboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnowboardRepository extends JpaRepository<Snowboard, Long> {

    // ----- show all -----
    List<Snowboard> findAllByOrderById();

    // ----- search -----
    List<Snowboard> findAllByNameContainingIgnoreCase(String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<Snowboard> findAllByOrderBySize();
}
