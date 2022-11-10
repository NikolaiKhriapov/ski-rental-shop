package my.project.skirentalshop.security.applicationUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByEmail(String email);

    List<ApplicationUser> findAllByApplicationUserRoleNot(ApplicationUserRole role);

    List<ApplicationUser> findAllByClientNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String search1,
                                                                                             String search2);
}
