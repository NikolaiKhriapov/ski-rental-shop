package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.BookingRiderEquipmentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRiderEquipmentLinkRepository extends JpaRepository<BookingRiderEquipmentLink, Long> {
}
