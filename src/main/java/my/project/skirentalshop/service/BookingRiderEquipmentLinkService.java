package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.BookingRiderEquipmentLinkRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingRiderEquipmentLinkService {

    private final BookingRiderEquipmentLinkRepository bookingRiderEquipmentLinkRepository;
    private final BookingService bookingService;

    BookingRiderEquipmentLinkService(BookingRiderEquipmentLinkRepository bookingRiderEquipmentLinkRepository,
                                     BookingService bookingService) {
        this.bookingRiderEquipmentLinkRepository = bookingRiderEquipmentLinkRepository;
        this.bookingService = bookingService;
    }

    public void save(BookingRiderEquipmentLink link) {
        bookingRiderEquipmentLinkRepository.save(link);
    }

    public void delete(BookingRiderEquipmentLink bookingRiderEquipmentLink) {
        bookingRiderEquipmentLinkRepository.delete(bookingRiderEquipmentLink);
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Booking booking, Rider rider) {
        return bookingRiderEquipmentLinkRepository.findByBookingIdAndRiderId(booking.getId(), rider.getId());
    }

    public Booking showOneBookingById(Long bookingId) {
        return bookingService.showOneBookingById(bookingId);
    }

    public List<Booking> showAllBookings() {
        return bookingService.showAllBookings();
    }

    public List<Booking> showAllBookingsForClient(String email) {
        return bookingService.showAllBookingsForClient(email);
    }

    public List<Rider> getListOfRiders(Long bookingId) {
        List<Rider> listOfRiders = new ArrayList<>();
        Booking booking = showOneBookingById(bookingId);

        for (BookingRiderEquipmentLink oneLink : booking.getListOfBookingRiderEquipmentLinks()) {
            listOfRiders.add(oneLink.getRider());
        }

        return listOfRiders;
    }
}
