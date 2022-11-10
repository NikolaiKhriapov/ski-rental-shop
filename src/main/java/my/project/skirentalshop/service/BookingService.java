package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.model.enums.EquipmentCondition;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import my.project.skirentalshop.repository.*;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          ClientRepository clientRepository,
                          EquipmentRepository equipmentRepository) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.equipmentRepository = equipmentRepository;
    }

    // ----- show all bookings -----
    public List<Booking> showAllBookings() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole applicationUserRole = applicationUser.getApplicationUserRole();

        switch (applicationUserRole) {
            case ADMIN -> {
                return bookingRepository.findAllByOrderById();
            }
            case CLIENT -> {
                return bookingRepository.findAllByClientId(applicationUser.getClient().getId());
            }
            default -> throw new IllegalArgumentException("ApplicationUserRole " + applicationUserRole + " not found!");
        }
    }

    // ----- add new booking -----
    public Booking createNewBooking() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole applicationUserRole = applicationUser.getApplicationUserRole();

        Booking newBooking = new Booking();
        switch (applicationUserRole) {
            case ADMIN -> {
                return newBooking;
            }
            case CLIENT -> {
                newBooking.setClient(applicationUser.getClient());
                return newBooking;
            }
            default -> throw new IllegalArgumentException("ApplicationUserRole " + applicationUserRole + " not found!");
        }
    }

    public void addNewBookingToDB(Booking newBooking) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole applicationUserRole = applicationUser.getApplicationUserRole();
        switch (applicationUserRole) {
            case ADMIN -> {
            }
            case CLIENT -> {
                Client updatedClient = clientRepository.findById(applicationUser.getClient().getId()).orElseThrow(() ->
                        new IllegalStateException("Client with id = " + applicationUser.getClient().getId() + " not found!"));
                updatedClient.setName(newBooking.getClient().getName());
                updatedClient.setPhone1(newBooking.getClient().getPhone1());

                newBooking.setClient(updatedClient);
                applicationUser.setClient(newBooking.getClient());
            }
            default -> throw new IllegalArgumentException("ApplicationUserRole " + applicationUserRole + " not found!");
        }
        bookingRepository.save(newBooking);
    }

    // ----- edit booking info / show one booking -----
    public Booking showOneBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Booking with id = " + id + " not found!"));
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
                //add all unique riders from all bookings of the client
                for (Booking oneBooking : allBookingsForClient) {
                    for (Rider oneRider : getListOfRiders(oneBooking.getId())) {
                        if (!allRidersForClient.contains(oneRider)) {
                            allRidersForClient.add(oneRider);
                        }
                    }
                }
                //remove all riders that appear in client bookings for the same time
                for (Booking oneBooking : allBookingsForTheSameTime) {
                    allRidersForClient.removeAll(getListOfRiders(oneBooking.getId()));
                }
                //remove all riders that already appear in this booking
                allRidersForClient.removeAll(getListOfRiders(bookingToBeUpdated.getId()));

                return allRidersForClient;
            }
            default -> throw new IllegalArgumentException("ApplicationUserRole " + role + " does not exist");
        }
    }

    public List<Equipment> showAllAvailableEquipmentByType(Booking booking, TypesOfEquipment typeOfEquipment) {
        //get all equipment by type
        List<Equipment> listOfAvailableEquipment = equipmentRepository.findAllByTypeOrderBySize(typeOfEquipment);
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableEquipment.removeIf(oneEquipment ->
                oneEquipment.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneEquipment.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneEquipment.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking oneBooking : showAllBookings()) {
            if (checkIfBookingsOverlap(booking, oneBooking)) {
                for (Rider rider : getListOfRiders(oneBooking.getId())) {
                    BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(oneBooking, rider.getId());
                    switch (typeOfEquipment) {
                        case SNOWBOARD ->
                                listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSnowboard());
                        case SNOWBOARD_BOOTS ->
                                listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSnowboardBoots());
                        case SKI -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSki());
                        case SKI_BOOTS ->
                                listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSkiBoots());
                        case HELMET -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getHelmet());
                        case JACKET -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getJacket());
                        case GLOVES -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getGloves());
                        case PANTS -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getPants());
                        case PROTECTIVE_SHORTS ->
                                listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getProtectiveShorts());
                        case KNEE_PROTECTION ->
                                listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getKneeProtection());
                    }
                }
            }
        }
        return listOfAvailableEquipment;
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Booking booking, Long riderId) {
        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();

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

        for (BookingRiderEquipmentLink oneLink : booking.getListOfBookingRiderEquipmentLinks()) {
            listOfRiders.add(oneLink.getRider());
        }

        return listOfRiders;
    }

    public void resetListOfRiders(Booking bookingToBeUpdated, Long bookingToUpdateFromId) {
        //because we did not populate booking.listOfBookingRiderEquipmentLinks, it became NULL after pulling it from
        //the view, and after failing validation we are being thrown to the view again, therefore we need to populate
        //the list again
        Booking bookingToUpdateFrom = showOneBookingById(bookingToUpdateFromId);
        List<BookingRiderEquipmentLink> linksToUpdateFrom = bookingToUpdateFrom.getListOfBookingRiderEquipmentLinks();
        bookingToBeUpdated.setListOfBookingRiderEquipmentLinks(linksToUpdateFrom);
    }

    public void updateBookingById(Long bookingToBeUpdatedId, Booking updatedBookingInfo) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);
        //update client
        bookingToBeUpdated.getClient().setName(updatedBookingInfo.getClient().getName());
        bookingToBeUpdated.getClient().setPhone1(updatedBookingInfo.getClient().getPhone1());
        //update applicationUser, if needed (applicationUser gets updated anyway, but after reauthorization)
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole applicationUserRole = applicationUser.getApplicationUserRole();
        switch (applicationUserRole) {
            case ADMIN -> {
            }
            case CLIENT -> applicationUser.setClient(bookingToBeUpdated.getClient());
            default -> throw new IllegalArgumentException("ApplicationUserRole " + applicationUserRole + " not found!");
        }
        //update booking
        bookingToBeUpdated.setDateOfArrival(updatedBookingInfo.getDateOfArrival());
        bookingToBeUpdated.setDateOfReturn(updatedBookingInfo.getDateOfReturn());
        bookingRepository.save(bookingToBeUpdated);
    }

    public void updateRiderRequestedEquipment(Long bookingToBeUpdatedId,
                                              Long riderToBeUpdatedId,
                                              BookingRiderEquipmentLink updatedLink) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);

        for (BookingRiderEquipmentLink oneLink : bookingToBeUpdated.getListOfBookingRiderEquipmentLinks()) {
            if (oneLink.getRider().getId().equals(riderToBeUpdatedId)) {
                oneLink.setRiderRequestedEquipment(updatedLink.getRiderRequestedEquipment());
            }
        }
        bookingRepository.save(bookingToBeUpdated);
    }

    public void setRiderAssignedEquipment(Long bookingToBeUpdatedId,
                                          Long riderToBeUpdatedId,
                                          RiderAssignedEquipment updatedRiderAssignedEquipment) {
        Booking booking = showOneBookingById(bookingToBeUpdatedId);

        BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(booking, riderToBeUpdatedId);
        RiderAssignedEquipment riderAssignedEquipment = link.getRiderAssignedEquipment();

        if (updatedRiderAssignedEquipment.getSnowboard().getId() != null) {
            riderAssignedEquipment.setSnowboard(updatedRiderAssignedEquipment.getSnowboard());
        }
        if (updatedRiderAssignedEquipment.getSnowboardBoots().getId() != null) {
            riderAssignedEquipment.setSnowboardBoots(updatedRiderAssignedEquipment.getSnowboardBoots());
        }
        if (updatedRiderAssignedEquipment.getSki().getId() != null) {
            riderAssignedEquipment.setSki(updatedRiderAssignedEquipment.getSki());
        }
        if (updatedRiderAssignedEquipment.getSkiBoots().getId() != null) {
            riderAssignedEquipment.setSkiBoots(updatedRiderAssignedEquipment.getSkiBoots());
        }
        if (updatedRiderAssignedEquipment.getHelmet().getId() != null) {
            riderAssignedEquipment.setHelmet(updatedRiderAssignedEquipment.getHelmet());
        }
        if (updatedRiderAssignedEquipment.getJacket().getId() != null) {
            riderAssignedEquipment.setJacket(updatedRiderAssignedEquipment.getJacket());
        }
        if (updatedRiderAssignedEquipment.getGloves().getId() != null) {
            riderAssignedEquipment.setGloves(updatedRiderAssignedEquipment.getGloves());
        }
        if (updatedRiderAssignedEquipment.getPants().getId() != null) {
            riderAssignedEquipment.setPants(updatedRiderAssignedEquipment.getPants());
        }
        if (updatedRiderAssignedEquipment.getProtectiveShorts().getId() != null) {
            riderAssignedEquipment.setProtectiveShorts(updatedRiderAssignedEquipment.getProtectiveShorts());
        }
        if (updatedRiderAssignedEquipment.getKneeProtection().getId() != null) {
            riderAssignedEquipment.setKneeProtection(updatedRiderAssignedEquipment.getKneeProtection());
        }

        link.setRiderAssignedEquipment(riderAssignedEquipment);
        booking.getListOfBookingRiderEquipmentLinks().remove(link);
        booking.getListOfBookingRiderEquipmentLinks().add(link);
        bookingRepository.save(booking);
    }

    public void removeRiderFromBooking(Long bookingToBeUpdatedId, Long riderToBeRemovedId) {
        Booking booking = showOneBookingById(bookingToBeUpdatedId);

        BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(booking, riderToBeRemovedId);
        unassignRiderAssignedEquipment(booking, link);

        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();
        links.remove(link);

        bookingRepository.save(booking);
    }

    public void unassignRiderAssignedEquipment(Booking booking, BookingRiderEquipmentLink link) {
        RiderAssignedEquipment riderAssignedEquipment = link.getRiderAssignedEquipment();

        riderAssignedEquipment.setSnowboard(null);
        riderAssignedEquipment.setSnowboardBoots(null);
        riderAssignedEquipment.setSki(null);
        riderAssignedEquipment.setSkiBoots(null);
        riderAssignedEquipment.setHelmet(null);
        riderAssignedEquipment.setJacket(null);
        riderAssignedEquipment.setGloves(null);
        riderAssignedEquipment.setPants(null);
        riderAssignedEquipment.setProtectiveShorts(null);
        riderAssignedEquipment.setKneeProtection(null);

        link.setRiderAssignedEquipment(riderAssignedEquipment);
        booking.getListOfBookingRiderEquipmentLinks().remove(link);
        booking.getListOfBookingRiderEquipmentLinks().add(link);
        bookingRepository.save(booking);
    }

    public void addExistingRiderToBooking(Long bookingId, Long riderId) {
        Booking booking = showOneBookingById(bookingId);
        Rider rider = showOneRiderById(riderId);

        booking.getListOfBookingRiderEquipmentLinks()
                .add(new BookingRiderEquipmentLink(booking, rider, new RiderAssignedEquipment(), new ArrayList<>()));
        bookingRepository.save(booking);
    }

    public Rider showOneRiderById(Long riderId) {
        //workaround, just so not to use riderService.showOneRiderById(Long riderId)
        //1 and only 1 rider will be found
        return showAllBookings().stream()
                .map(booking -> Objects.requireNonNull(booking.getListOfBookingRiderEquipmentLinks().stream()
                                .filter(link -> Objects.equals(link.getRider().getId(), riderId))
                                .findAny().orElse(null))
                        .getRider())
                .findAny()
                .orElse(null);
    }

    // ----- delete booking -----
    public void deleteBookingById(Long bookingToBeDeletedId) {
        // remove riders from booking
        for (Rider oneRider : getListOfRiders(bookingToBeDeletedId)) {
            removeRiderFromBooking(bookingToBeDeletedId, oneRider.getId());
        }
        // delete booking
        Booking bookingToBeDeleted = showOneBookingById(bookingToBeDeletedId);
        bookingToBeDeleted.setClient(null);
        bookingRepository.save(bookingToBeDeleted);
        bookingRepository.delete(bookingToBeDeleted);
    }

    // ----- mark booking completed -----
    public void changeBookingCompleted(Long bookingId) {
        Booking booking = showOneBookingById(bookingId);
        booking.setCompleted(!booking.isCompleted());
        bookingRepository.save(booking);
    }

    // ----- search -----
    public List<Booking> showBookingsBySearch(String search) {
        return bookingRepository.findAllByClientNameContainingIgnoreCaseOrClientPhone1ContainingIgnoreCaseOrClientPhone2ContainingIgnoreCase(
                search, search, search);
    }

    // ----- sort -----
    public List<Booking> sortAllBookingsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return bookingRepository.findAll(sort);
    }

    // ----- AdminHomeController / show all incomplete bookings -----
    public List<Booking> showAllIncompleteBookings() {
        return bookingRepository.findAllByCompletedFalseOrderByDateOfArrivalAsc();
    }

    // ----- AdminHomeController / show all current bookings -----
    public List<Booking> showAllCurrentBookings() {
        return bookingRepository.findAllByDateOfArrivalBeforeAndDateOfReturnAfter(new Date(), new Date());
    }

    // ----- AdminHomeController / show bookings for the date -----
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

    // ----- ClientHomeController / show upcoming bookings for the client -----
    public List<Booking> showCurrentBookingsForClient(Long clientId) {
        return bookingRepository.findAllByClientIdAndDateOfReturnAfter(clientId, new Date());
    }
}
