package my.project.skirentalshop.repository;import my.project.skirentalshop.entity.Booking;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.stereotype.Repository;import java.util.Date;import java.util.List;@Repositorypublic interface BookingRepository extends JpaRepository<Booking, Long> {    List<Booking> findAllByOrderById();    List<Booking> findAllByClientNameContainingIgnoreCaseOrClientPhone1ContainingIgnoreCaseOrClientPhone2ContainingIgnoreCase(            String search1, String search2, String search3);    List<Booking> findByDateOfArrivalIsBetween(Date dateFrom, Date dateTo);    List<Booking> findAllByCompletedFalseOrderByDateOfArrivalAsc();    List<Booking> findAllByDateOfArrivalBeforeAndDateOfReturnAfter(Date dateNow1, Date dateNow2);    List<Booking> findAllByClientIdAndDateOfReturnAfter(Long clientId, Date dateNow);    List<Booking> findAllByClientId(Long clientId);}