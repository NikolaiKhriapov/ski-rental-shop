package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // ----- show all -----
    List<Client> findAllByOrderById();

    // ----- search -----
    List<Client> findAllBySurnameContainingIgnoreCaseOrPhone1ContainingIgnoreCaseOrPhone2ContainingIgnoreCase(
            String search1, String search2, String search3
    );
}
