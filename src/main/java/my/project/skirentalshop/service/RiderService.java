package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.RiderRepository;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RiderService {

    private final RiderRepository riderRepository;
    private final BookingService bookingService; //TODO: remove

    @Autowired
    public RiderService(RiderRepository riderRepository,
                        BookingService bookingService) {
        this.riderRepository = riderRepository;
        this.bookingService = bookingService;
    }

    // ----- show all -----
    public List<Rider> showAllRiders() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole role = applicationUser.getApplicationUserRole();
        switch (role) {
            case ADMIN -> {
                return riderRepository.findAllByOrderById();
            }
            case CLIENT -> {
                List<Rider> allRidersForClient = new ArrayList<>();

                List<Rider> listOfAllRiders = riderRepository.findAllByOrderById();
                for (Rider oneRider : listOfAllRiders) {
                    for (BookingRiderEquipmentLink oneLink : oneRider.getListOfBookingRiderEquipmentLinks()) {
                        if (Objects.equals(oneLink.getBooking().getClient().getId(), applicationUser.getClient().getId())) {
                            if (!allRidersForClient.contains(oneRider)) {
                                allRidersForClient.add(oneRider);
                            }
                        }
                    }
                }
                return allRidersForClient;
            }
            default -> throw new IllegalArgumentException("ApplicationUserRole " + role + " not found!");
        }
    }

    // ----- add new -----
    public void addNewRiderToDB(Rider rider, Long bookingId) {
        riderRepository.save(rider);

        if (bookingId != null) {
            List<BookingRiderEquipmentLink> links = rider.getListOfBookingRiderEquipmentLinks();
            links.add(new BookingRiderEquipmentLink(
                    bookingService.showOneBookingById(bookingId),
                    rider,
                    new ArrayList<>(),
                    new RiderAssignedEquipment())
            );
            riderRepository.save(rider);
        }
    }

    // ----- edit -----
    public Rider showOneRiderById(Long id) {
        return riderRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Rider with id = " + id + " not found!"));
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Long bookingId, Long riderId) {
        if (bookingId == null) {
            return null;
        } else {
            Rider rider = showOneRiderById(riderId);
            List<BookingRiderEquipmentLink> links = rider.getListOfBookingRiderEquipmentLinks();

            return links.stream()
                    .filter(link -> Objects.equals(link.getBooking().getId(), bookingId))
                    .findAny()
                    .orElse(null);
        }
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
}
