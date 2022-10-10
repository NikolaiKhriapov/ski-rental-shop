package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // ----- show all bookings -----
    public List<Booking> showAllBookings() {
        return bookingRepository.findAllByOrderById();
    }

    // ----- add new booking -----
    public void addNewBookingToDB(Booking newBooking) {
        bookingRepository.save(newBooking);
    }

    public void addNewRiderToBooking(Long bookingToBeUpdatedId, Rider rider) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);
        bookingToBeUpdated.getListOfRiders().add(rider);
        bookingRepository.save(bookingToBeUpdated);
    }

    // ----- edit booking info -----
    public Booking showOneBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Booking with id = " + id + " not found!"));
    }

    public void updateBookingById(Long bookingToBeUpdatedId, Booking updatedBooking) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);

        bookingToBeUpdated.setClient(updatedBooking.getClient());
        bookingToBeUpdated.setDateOfArrival(updatedBooking.getDateOfArrival());
        bookingToBeUpdated.setDateOfReturn(updatedBooking.getDateOfReturn());

        bookingRepository.save(bookingToBeUpdated);
    }

    public void addExistingRiderToBooking(Booking bookingToBeUpdated, Rider riderToBeAdded) {
        for (Rider oneRider : bookingToBeUpdated.getListOfRiders()) {
            if (oneRider.getId().equals(riderToBeAdded.getId())) {
                return;
            }
        }
        bookingToBeUpdated.getListOfRiders().add(riderToBeAdded);
        bookingRepository.save(bookingToBeUpdated);
    }

    public void removeRiderFromBooking(Booking bookingToBeUpdated, Rider riderToBeRemoved) {
        bookingToBeUpdated.getListOfRiders().remove(riderToBeRemoved);
        bookingRepository.save(bookingToBeUpdated);
    }

    // ----- delete booking -----
    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    // ----- mark booking completed -----
    public void changeBookingCompleted(Long bookingId) {
        Booking bookingToChangeCompleted = showOneBookingById(bookingId);
        bookingToChangeCompleted.setCompleted(bookingToChangeCompleted.isCompleted() ? false : true);
        bookingRepository.save(bookingToChangeCompleted);
    }

    // ----- search -----
    public List<Booking> showBookingsBySearch(String search) {
        return bookingRepository.findAllByClientSurnameContainingIgnoreCaseOrClientPhone1ContainingIgnoreCaseOrClientPhone2ContainingIgnoreCase(
                search, search, search);
    }

    // ----- sort -----
    public List<Booking> sortAllBookingsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return bookingRepository.findAll(sort);
    }

    // ----- show bookings for the date -----
    public Date[] getTodayBeginningAndEnd() {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());

        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());

        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);

        return new Date[]{c1.getTime(), c2.getTime()};
    }

    public Date[] getTomorrowBeginningAndEnd() {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        c1.add(Calendar.DATE, 1);

        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        c2.add(Calendar.DATE, 1);

        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);

        return new Date[]{c1.getTime(), c2.getTime()};
    }

    public List<Booking> showBookingsForTheDate(Date dateFrom, Date dateTo) {
        return bookingRepository.findByDateOfArrivalIsBetween(dateFrom, dateTo);
    }

    // ----- show incomplete bookings -----
    public List<Booking> showAllIncompleteBookings() {
        return bookingRepository.findAllByCompletedFalseOrderByDateOfArrivalAsc();
    }
}
