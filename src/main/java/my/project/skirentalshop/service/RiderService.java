package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.RiderRepository;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiderService {

    private final RiderRepository riderRepository;
    private final BookingRiderEquipmentLinkService bookingRiderEquipmentLinkService;

    @Autowired
    public RiderService(RiderRepository riderRepository,
                        BookingRiderEquipmentLinkService bookingRiderEquipmentLinkService) {
        this.riderRepository = riderRepository;
        this.bookingRiderEquipmentLinkService = bookingRiderEquipmentLinkService;
    }

    // ----- show all -----
    public List<Rider> showAllRiders() {
        return riderRepository.findAllByOrderById();
    }

    public List<Booking> showAllBookings() {
        return bookingRiderEquipmentLinkService.showAllBookings();
    }

    public void addRiderToBooking(Long bookingId, Rider rider) {
        bookingRiderEquipmentLinkService.save(new BookingRiderEquipmentLink(
                showOneBookingById(bookingId),
                rider,
                new ArrayList<>(),
                new RiderAssignedEquipment())
        );
    }

    // ----- add new -----
    public void addNewRiderToDB(Rider rider) {
        riderRepository.save(rider);
    }

    // ----- edit -----
    public Rider showOneRiderById(Long id) {
        return riderRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Rider with id = " + id + " not found!"));
    }

    public Booking showOneBookingById(Long id) {
        return bookingRiderEquipmentLinkService.showOneBookingById(id);
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Booking booking, Rider rider) {
        return bookingRiderEquipmentLinkService.getBookingRiderEquipmentLink(booking, rider);
    }

    public void updateRiderById(Long id, Rider updatedRider) {
        Rider riderToBeUpdated = showOneRiderById(id);

        riderToBeUpdated.setName(updatedRider.getName());
        riderToBeUpdated.setFootSize(updatedRider.getFootSize());
        riderToBeUpdated.setSex(updatedRider.getSex());
        riderToBeUpdated.setHeight(updatedRider.getHeight());
        riderToBeUpdated.setWeight(updatedRider.getWeight());

        riderRepository.save(riderToBeUpdated);
    }

    // ----- delete -----
    public void deleteRiderById(Long id) {
        riderRepository.deleteById(id);
    }

    // ----- search -----
    public List<Rider> showRidersBySearch(String search) {
        return riderRepository.findAllByNameContainingIgnoreCaseOrderById(search);
    }

    // ----- sort -----
    public List<Rider> sortAllRidersByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return riderRepository.findAll(sort);
    }

    // ----- ClientRiderController / show all -----
    public List<Rider> showAllRidersForCurrentClient() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long clientId = applicationUser.getClient().getId();

        List<Booking> allBookingsForClient = bookingRiderEquipmentLinkService.showAllBookingsForClient(clientId);

        List<Rider> allRidersForClient = new ArrayList<>();
        for (Booking oneBooking : allBookingsForClient) {
            for (Rider oneRider : bookingRiderEquipmentLinkService.getListOfRiders(oneBooking.getId())) {
                if (!allRidersForClient.contains(oneRider)) {
                    allRidersForClient.add(oneRider);
                }
            }
        }
        return allRidersForClient;
    }
}
