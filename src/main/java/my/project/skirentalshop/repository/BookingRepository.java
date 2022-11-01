package my.project.skirentalshop.repository;import my.project.skirentalshop.model.Booking;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.stereotype.Repository;import java.util.Date;import java.util.List;@Repositorypublic interface BookingRepository extends JpaRepository<Booking, Long> {    // ----- show all -----    List<Booking> findAllByOrderById();    // ----- search -----    List<Booking> findAllByClientNameContainingIgnoreCaseOrClientPhone1ContainingIgnoreCaseOrClientPhone2ContainingIgnoreCase(            String search1, String search2, String search3);    // ----- show bookings for the date    List<Booking> findByDateOfArrivalIsBetween(Date dateFrom, Date dateTo);    // ----- show all incomplete bookings -----    List<Booking> findAllByCompletedFalseOrderByDateOfArrivalAsc();    // ----- show all current bookings -----    List<Booking> findAllByDateOfArrivalBeforeAndDateOfReturnAfter(Date dateNow1, Date dateNow2);    // ----- show upcoming bookings for the client -----    List<Booking> findAllByClientIdAndDateOfReturnAfter(Long clientId, Date dateNow);    // ----- show all bookings for the client -----    List<Booking> findAllByClientId(Long clientId);}