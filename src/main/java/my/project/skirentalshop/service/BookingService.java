package my.project.skirentalshop.service;

import my.project.skirentalshop.dto.RiderAssignedEquipmentDTO;
import my.project.skirentalshop.entity.*;
import my.project.skirentalshop.entity.enums.EquipmentCondition;
import my.project.skirentalshop.entity.enums.TypesOfEquipment;
import my.project.skirentalshop.repository.*;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final EquipmentRepository equipmentRepository;
    private final RiderRepository riderRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          ClientRepository clientRepository,
                          EquipmentRepository equipmentRepository, RiderRepository riderRepository) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.equipmentRepository = equipmentRepository;
        this.riderRepository = riderRepository;
    }

    public List<Booking> showAllBookings() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole role = applicationUser.getApplicationUserRole();
        switch (role) {
            case ADMIN -> {
                return bookingRepository.findAllByOrderById();
            }
            case CLIENT -> {
                return bookingRepository.findAllByClientId(applicationUser.getClient().getId());
            }
            default -> throw new IllegalArgumentException(
                    getExceptionMessage("exception.app-user.role-not-found", role.toString())
            );
        }
    }

    public Booking createNewBooking() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole role = applicationUser.getApplicationUserRole();

        Booking newBooking = new Booking();
        switch (role) {
            case ADMIN -> {
                return newBooking;
            }
            case CLIENT -> {
                newBooking.setClient(applicationUser.getClient());
                return newBooking;
            }
            default -> throw new IllegalArgumentException(
                    getExceptionMessage("exception.app-user.role-not-found", role.toString())
            );
        }
    }

    public void addNewBookingToDB(Booking newBooking) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole role = applicationUser.getApplicationUserRole();
        switch (role) {
            case ADMIN -> {
            }
            case CLIENT -> {
                Long clientId = applicationUser.getClient().getId();
                Client updatedClient = clientRepository.findById(applicationUser.getClient().getId()).orElseThrow(() ->
                        new IllegalStateException(getExceptionMessage(
                                "exception.client.id-not-found", clientId.toString()
                        ))
                );
                updatedClient.setName(newBooking.getClient().getName());
                updatedClient.setPhone1(newBooking.getClient().getPhone1());

                newBooking.setClient(updatedClient);
                applicationUser.setClient(newBooking.getClient());
            }
            default -> throw new IllegalArgumentException(
                    getExceptionMessage("exception.app-user.role-not-found", role.toString())
            );
        }
        bookingRepository.save(newBooking);
    }

    public Booking showOneBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalStateException(
                getExceptionMessage("exception.booking.id-not-found", bookingId.toString())
        ));
    }

    public void resetListOfRiders(Booking bookingToBeUpdated, Long bookingToUpdateFromId) {
        Booking bookingToUpdateFrom = showOneBookingById(bookingToUpdateFromId);
        List<BookingRiderEquipmentLink> linksToUpdateFrom = bookingToUpdateFrom.getListOfBookingRiderEquipmentLinks();
        if (linksToUpdateFrom == null) {
            linksToUpdateFrom = new ArrayList<>();
        }
        bookingToBeUpdated.setListOfBookingRiderEquipmentLinks(linksToUpdateFrom);
    }

    public List<Rider> showAvailableExistingRidersForClientForBooking(Long bookingToBeUpdatedId) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole role = applicationUser.getApplicationUserRole();

        switch (role) {
            case ADMIN -> {
                return null;
            }
            case CLIENT -> {
                Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);

                List<Rider> allRidersForClient = new ArrayList<>();
                List<Booking> allBookingsForClient = showAllBookings();
                List<Booking> allBookingsForTheSameTime = showBookingsForTheDate(
                        bookingToBeUpdated.getDateOfArrival(), bookingToBeUpdated.getDateOfReturn());

                for (Booking oneBooking : allBookingsForClient) {
                    for (Rider oneRider : getListOfRiders(oneBooking.getId())) {
                        if (!allRidersForClient.contains(oneRider)) {
                            allRidersForClient.add(oneRider);
                        }
                    }
                }

                for (Booking oneBooking : allBookingsForTheSameTime) {
                    allRidersForClient.removeAll(getListOfRiders(oneBooking.getId()));
                }

                allRidersForClient.removeAll(getListOfRiders(bookingToBeUpdated.getId()));

                return allRidersForClient;
            }
            default -> throw new IllegalArgumentException(
                    getExceptionMessage("exception.app-user.role-not-found", role.toString())
            );
        }
    }

    public void updateBookingById(Long bookingToBeUpdatedId, Booking updatedBookingInfo) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);

        bookingToBeUpdated.getClient().setName(updatedBookingInfo.getClient().getName());
        bookingToBeUpdated.getClient().setPhone1(updatedBookingInfo.getClient().getPhone1());

        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole role = applicationUser.getApplicationUserRole();
        switch (role) {
            case ADMIN -> {
            }
            case CLIENT -> applicationUser.setClient(bookingToBeUpdated.getClient());
            default -> throw new IllegalArgumentException(
                    getExceptionMessage("exception.app-user.role-not-found", role.toString())
            );
        }

        bookingToBeUpdated.setDateOfArrival(updatedBookingInfo.getDateOfArrival());
        bookingToBeUpdated.setDateOfReturn(updatedBookingInfo.getDateOfReturn());
        bookingRepository.save(bookingToBeUpdated);
    }

    public void updateRiderRequestedEquipment(Long bookingToBeUpdatedId,
                                              Long riderToBeUpdatedId,
                                              BookingRiderEquipmentLink updatedLink) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);

        List<BookingRiderEquipmentLink> links = bookingToBeUpdated.getListOfBookingRiderEquipmentLinks();
        if (links == null) {
            links = new ArrayList<>();
        }
        for (BookingRiderEquipmentLink oneLink : links) {
            if (oneLink.getRider().getId().equals(riderToBeUpdatedId)) {
                oneLink.setRiderRequestedEquipment(updatedLink.getRiderRequestedEquipment());
            }
        }
        bookingRepository.save(bookingToBeUpdated);
    }

    public void updateRiderAssignedEquipment(Long bookingToBeUpdatedId,
                                             Long riderToBeUpdatedId,
                                             RiderAssignedEquipmentDTO riderAssignedEquipmentDTO) {
        List<Equipment> riderAssignedEquipment = new ArrayList<>();
        if (riderAssignedEquipmentDTO.getSnowboard() != null) {
            if (riderAssignedEquipmentDTO.getSnowboard().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSnowboard());
            }
        }
        if (riderAssignedEquipmentDTO.getSnowboardBoots() != null) {
            if (riderAssignedEquipmentDTO.getSnowboardBoots().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSnowboardBoots());
            }
        }
        if (riderAssignedEquipmentDTO.getSki() != null) {
            if (riderAssignedEquipmentDTO.getSki().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSki());
            }
        }
        if (riderAssignedEquipmentDTO.getSkiBoots() != null) {
            if (riderAssignedEquipmentDTO.getSkiBoots().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSkiBoots());
            }
        }
        if (riderAssignedEquipmentDTO.getHelmet() != null) {
            if (riderAssignedEquipmentDTO.getHelmet().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getHelmet());
            }
        }
        if (riderAssignedEquipmentDTO.getJacket() != null) {
            if (riderAssignedEquipmentDTO.getJacket().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getJacket());
            }
        }
        if (riderAssignedEquipmentDTO.getGloves() != null) {
            if (riderAssignedEquipmentDTO.getGloves().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getGloves());
            }
        }
        if (riderAssignedEquipmentDTO.getPants() != null) {
            if (riderAssignedEquipmentDTO.getPants().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getPants());
            }
        }
        if (riderAssignedEquipmentDTO.getProtectiveShorts() != null) {
            if (riderAssignedEquipmentDTO.getProtectiveShorts().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getProtectiveShorts());
            }
        }
        if (riderAssignedEquipmentDTO.getKneeProtection() != null) {
            if (riderAssignedEquipmentDTO.getKneeProtection().getId() != null) {
                riderAssignedEquipment.add(riderAssignedEquipmentDTO.getKneeProtection());
            }
        }

        Booking booking = showOneBookingById(bookingToBeUpdatedId);
        getBookingRiderEquipmentLink(booking, riderToBeUpdatedId).setRiderAssignedEquipment(riderAssignedEquipment);
        bookingRepository.save(booking);
    }

    public void removeRiderFromBooking(Long bookingToBeUpdatedId, Long riderToBeRemovedId) {
        Booking booking = showOneBookingById(bookingToBeUpdatedId);
        BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(booking, riderToBeRemovedId);

        link.setRiderAssignedEquipment(new ArrayList<>());
        link.setBooking(null);
        link.setRider(null);

        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();
        if (links == null) {
            links = new ArrayList<>();
        }
        links.remove(link);

        bookingRepository.save(booking);
    }

    public void addExistingRiderToBooking(Long bookingId, Long riderId) {
        Booking booking = showOneBookingById(bookingId);
        Rider rider = showOneRiderById(riderId);

        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();
        if (links == null) {
            links = new ArrayList<>();
        }
        links.add(new BookingRiderEquipmentLink(booking, rider, new ArrayList<>(), new ArrayList<>()));
        bookingRepository.save(booking);
    }

    public void deleteBookingById(Long bookingToBeDeletedId) {
        for (Rider oneRider : getListOfRiders(bookingToBeDeletedId)) {
            removeRiderFromBooking(bookingToBeDeletedId, oneRider.getId());
        }
        Booking bookingToBeDeleted = showOneBookingById(bookingToBeDeletedId);
        bookingToBeDeleted.setClient(null);
        bookingRepository.save(bookingToBeDeleted);
        bookingRepository.delete(bookingToBeDeleted);
    }

    public void changeBookingCompleted(Long bookingId) {
        Booking booking = showOneBookingById(bookingId);
        booking.setCompleted(!booking.isCompleted());
        bookingRepository.save(booking);
    }

    public List<Booking> showBookingsBySearch(String search) {
        return bookingRepository.findAllByClientNameContainingIgnoreCaseOrClientPhone1ContainingIgnoreCaseOrClientPhone2ContainingIgnoreCase(
                search, search, search);
    }

    public List<Booking> sortAllBookingsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return bookingRepository.findAll(sort);
    }

    public List<Booking> showAllIncompleteBookings() {
        return bookingRepository.findAllByCompletedFalseOrderByDateOfArrivalAsc();
    }

    public List<Booking> showAllCurrentBookings() {
        return bookingRepository.findAllByDateOfArrivalBeforeAndDateOfReturnAfter(new Date(), new Date());
    }

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

    public List<Booking> showCurrentBookingsForClient(Long clientId) {
        return bookingRepository.findAllByClientIdAndDateOfReturnAfter(clientId, new Date());
    }

    public List<Equipment> showAllAvailableEquipmentByType(Booking booking, TypesOfEquipment typeOfEquipment) {
        List<Equipment> listOfAvailableEquipment = equipmentRepository.findAllByTypeOrderBySize(typeOfEquipment);

        listOfAvailableEquipment.removeIf(oneEquipment ->
                oneEquipment.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneEquipment.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneEquipment.getCondition().equals(EquipmentCondition.UNKNOWN));

        for (Booking oneBooking : showAllBookings()) {
            if (checkIfBookingsOverlap(booking, oneBooking)) {
                for (Rider rider : getListOfRiders(oneBooking.getId())) {
                    BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(oneBooking, rider.getId());
                    listOfAvailableEquipment.remove(getEquipmentByType(link.getRiderAssignedEquipment(), typeOfEquipment));
                }
            }
        }
        return listOfAvailableEquipment;
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Booking booking, Long riderId) {
        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();
        if (links == null) {
            links = new ArrayList<>();
        }

        return links.stream()
                .filter(link -> Objects.equals(link.getRider().getId(), riderId))
                .findAny()
                .orElse(null);
    }

    public boolean checkIfBookingsOverlap(Booking booking1, Booking booking2) {
        return ((booking1.getDateOfArrival().after(booking2.getDateOfArrival()) || booking1.getDateOfArrival().equals(booking2.getDateOfArrival())) &&
                (booking1.getDateOfArrival().before(booking2.getDateOfReturn()) || booking1.getDateOfArrival().equals(booking2.getDateOfReturn()))) ||
                ((booking1.getDateOfReturn().after(booking2.getDateOfArrival()) || booking1.getDateOfReturn().equals(booking2.getDateOfArrival())) &&
                        (booking1.getDateOfReturn().before(booking2.getDateOfReturn()) || booking1.getDateOfReturn().equals(booking2.getDateOfReturn()))) ||
                (booking1.getDateOfArrival().before(booking2.getDateOfArrival()) && booking1.getDateOfReturn().after(booking2.getDateOfReturn()));
    }

    public List<Rider> getListOfRiders(Long bookingId) {
        List<Rider> listOfRiders = new ArrayList<>();
        Booking booking = showOneBookingById(bookingId);

        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();
        if (links == null) {
            links = new ArrayList<>();
        }
        for (BookingRiderEquipmentLink oneLink : links) {
            listOfRiders.add(oneLink.getRider());
        }
        return listOfRiders;
    }

    public boolean containsEquipmentByType(List<Equipment> listOfEquipment, TypesOfEquipment type) {
        return listOfEquipment.stream().anyMatch(oneEquipment -> oneEquipment.getType() == type);
    }

    public Equipment getEquipmentByType(List<Equipment> listOfEquipment, TypesOfEquipment type) {
        List<Equipment> equipment = listOfEquipment.stream()
                .filter(oneEquipment -> oneEquipment.getType() == type)
                .toList();

        if (equipment.size() > 1) {
            throw new IllegalStateException(
                    getExceptionMessage("exception.equipment.many-of-type", type.toString())
            );
        } else if (equipment.size() < 1) {
            return null;
        } else {
            return equipment.get(0);
        }
    }

    public Rider showOneRiderById(Long riderId) {
        return riderRepository.findById(riderId).orElseThrow(() -> new IllegalStateException(
                getExceptionMessage("exception.rider.id-not-found", riderId.toString())
        ));
    }

    public String getExceptionMessage(String propertyKey, String parameter) {
        return String.format(
                ResourceBundle
                        .getBundle("exception", LocaleContextHolder.getLocale())
                        .getString(propertyKey),
                parameter
        );
    }
}
