package my.project.skirentalshop.repository;

import my.project.skirentalshop.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    // ----- show all -----
    List<Rider> findAllByOrderById();

    // ----- search -----
    List<Rider> findAllByNameContainingIgnoreCaseOrderById(String search);
}
