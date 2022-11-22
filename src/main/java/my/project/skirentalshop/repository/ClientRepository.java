package my.project.skirentalshop.repository;

import my.project.skirentalshop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByOrderById();

    List<Client> findAllByNameContainingIgnoreCaseOrPhone1ContainingIgnoreCaseOrPhone2ContainingIgnoreCase(
            String search1, String search2, String search3
    );
}
