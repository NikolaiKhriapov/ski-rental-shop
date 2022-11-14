package my.project.skirentalshop.service;

import my.project.skirentalshop.entity.*;
import my.project.skirentalshop.repository.BookingRepository;
import my.project.skirentalshop.repository.RiderRepository;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RiderService {

    private final RiderRepository riderRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public RiderService(RiderRepository riderRepository,
                        BookingRepository bookingRepository) {
        this.riderRepository = riderRepository;
        this.bookingRepository = bookingRepository;
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
                    List<BookingRiderEquipmentLink> links = oneRider.getListOfBookingRiderEquipmentLinks();
                    if (links == null) {
                        links = new ArrayList<>();
                    }
                    for (BookingRiderEquipmentLink oneLink : links) {
                        if (Objects.equals(oneLink.getBooking().getClient().getId(), applicationUser.getClient().getId())) {
                            if (!allRidersForClient.contains(oneRider)) {
                                allRidersForClient.add(oneRider);
                            }
                        }
                    }
                }
                return allRidersForClient;
            }
            default -> throw new IllegalArgumentException(
                    getExceptionMessage("exception.app-user.role-not-found", role.toString())
            );
        }
    }

    // ----- add new -----
    public Booking showOneBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalStateException(
                getExceptionMessage("exception.booking.id-not-found", bookingId.toString())
        ));
    }

    public void addNewRiderToDB(Rider rider, Long bookingId) {
        riderRepository.save(rider);
        if (bookingId != null) {
            List<BookingRiderEquipmentLink> links = rider.getListOfBookingRiderEquipmentLinks();
            if (links == null) {
                links = new ArrayList<>();
            }
            links.add(new BookingRiderEquipmentLink(
                    showOneBookingById(bookingId),
                    rider,
                    new ArrayList<>(),
                    new ArrayList<>())
            );
            rider.setListOfBookingRiderEquipmentLinks(links);
            riderRepository.save(rider);
        }
    }

    // ----- edit -----
    public Rider showOneRiderById(Long riderId) {
        return riderRepository.findById(riderId).orElseThrow(() -> new IllegalStateException(
                getExceptionMessage("exception.rider.id-not-found", riderId.toString())
        ));
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Long bookingId, Long riderId) {
        if (bookingId == null) {
            return null;
        } else {
            Rider rider = showOneRiderById(riderId);
            List<BookingRiderEquipmentLink> links = rider.getListOfBookingRiderEquipmentLinks();
            if (links == null) {
                links = new ArrayList<>();
            }
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
    public void deleteRiderById(Long riderId) {
        try {
            riderRepository.deleteById(riderId);
        } catch (DataIntegrityViolationException e) {
            String riderName = showOneRiderById(riderId).getName();
            throw new DataIntegrityViolationException(
                    getExceptionMessage("exception.rider.cannot-be-deleted", riderName)
            );
        }
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

    // ----- supplementary -----
    public String getExceptionMessage(String propertyKey, String parameter) {
        return String.format(
                ResourceBundle
                        .getBundle("exception", LocaleContextHolder.getLocale())
                        .getString(propertyKey),
                parameter
        );
    }
}
