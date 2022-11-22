package my.project.skirentalshop.repository;

import my.project.skirentalshop.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    List<Rider> findAllByOrderById();

    List<Rider> findAllByNameContainingIgnoreCaseOrderById(String search);
}
