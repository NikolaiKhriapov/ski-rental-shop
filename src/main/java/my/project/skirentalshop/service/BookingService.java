package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.model.enums.EquipmentCondition;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import my.project.skirentalshop.repository.BookingRepository;
import my.project.skirentalshop.repository.BookingRiderEquipmentLinkRepository;
import my.project.skirentalshop.repository.EquipmentRepository;
import my.project.skirentalshop.repository.RiderRepository;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingRiderEquipmentLinkRepository bookingRiderEquipmentLinkRepository;
    private final RiderRepository riderRepository;
    private final EquipmentRepository equipmentRepository;

    private final ApplicationUserService applicationUserService;
    private final ClientService clientService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          BookingRiderEquipmentLinkRepository bookingRiderEquipmentLinkRepository,
                          RiderRepository riderRepository,
                          EquipmentRepository equipmentRepository,
                          ApplicationUserService applicationUserService,
                          ClientService clientService) {
        this.bookingRepository = bookingRepository;
        this.bookingRiderEquipmentLinkRepository = bookingRiderEquipmentLinkRepository;
        this.riderRepository = riderRepository;
        this.equipmentRepository = equipmentRepository;
        this.applicationUserService = applicationUserService;
        this.clientService = clientService;
    }

    // ----- show all bookings -----
    public List<Booking> showAllBookings() {
        return bookingRepository.findAllByOrderById();
    }

    // ----- add new booking -----
    public void addNewBookingToDB(Booking newBooking) {
        bookingRepository.save(newBooking);
    }

    // ----- edit booking info / show one booking -----
    public Booking showOneBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Booking with id = " + id + " not found!"));
    }

    public List<Rider> showAvailableExistingRidersForClientForBooking(Long bookingToBeUpdatedId) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);

        List<Rider> allRidersForClient = new ArrayList<>();
        List<Booking> allBookingsForClient = showAllBookingsForClient(bookingToBeUpdated.getClient().getEmail());
        List<Booking> allBookingsForClientForTheSameTime = showBookingsForTheDate(
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
        for (Booking oneBooking : allBookingsForClientForTheSameTime) {
            allRidersForClient.removeAll(getListOfRiders(oneBooking.getId()));
        }
        //remove all riders that already appear in this booking
        allRidersForClient.removeAll(getListOfRiders(bookingToBeUpdated.getId()));

        return allRidersForClient;
    }

    public List<Booking> showAllBookingsForClient(String email) {
        return bookingRepository.findAllByClientEmail(email);
    }

    public boolean checkIfBookingsOverlap(Booking booking1, Booking booking2) {
        return ((booking1.getDateOfArrival().after(booking2.getDateOfArrival()) || booking1.getDateOfArrival().equals(booking2.getDateOfArrival())) &&
                (booking1.getDateOfArrival().before(booking2.getDateOfReturn()) || booking1.getDateOfArrival().equals(booking2.getDateOfReturn()))) ||
                ((booking1.getDateOfReturn().after(booking2.getDateOfArrival()) || booking1.getDateOfReturn().equals(booking2.getDateOfArrival())) &&
                        (booking1.getDateOfReturn().before(booking2.getDateOfReturn()) || booking1.getDateOfReturn().equals(booking2.getDateOfReturn()))) ||
                (booking1.getDateOfArrival().before(booking2.getDateOfArrival()) && booking1.getDateOfReturn().after(booking2.getDateOfReturn()));
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
                    BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(oneBooking, rider);
                    switch (typeOfEquipment) {
                        case SNOWBOARD -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSnowboard());
                        case SNOWBOARD_BOOTS -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSnowboardBoots());
                        case SKI -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSki());
                        case SKI_BOOTS -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getSkiBoots());
                        case HELMET -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getHelmet());
                        case JACKET -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getJacket());
                        case GLOVES -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getGloves());
                        case PANTS -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getPants());
                        case PROTECTIVE_SHORTS -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getProtectiveShorts());
                        case KNEE_PROTECTION -> listOfAvailableEquipment.remove(link.getRiderAssignedEquipment().getKneeProtection());
                    }
                }
            }
        }
        return listOfAvailableEquipment;
    }

    public List<Rider> getListOfRiders(Long bookingId) {
        List<Rider> listOfRiders = new ArrayList<>();
        Booking booking = showOneBookingById(bookingId);

        for (BookingRiderEquipmentLink oneLink : booking.getListOfBookingRiderEquipmentLinks()) {
            listOfRiders.add(oneLink.getRider());
        }

        return listOfRiders;
    }

    public void setListOfRiders(Booking booking, List<Rider> updatedListOfRiders) {
        //delete all current bookingRiderEquipmentLinks for the booking
        bookingRiderEquipmentLinkRepository.deleteAll(booking.getListOfBookingRiderEquipmentLinks());
        //create new bookingRiderEquipmentLinks for the riders (without equipment)
        for (Rider oneRider : updatedListOfRiders) {
            new BookingRiderEquipmentLink(booking, oneRider, new ArrayList<>(), new RiderAssignedEquipment());
        }
    }

    public void updateBookingById(Long bookingToBeUpdatedId, Booking updatedBookingInfo) {
        Booking bookingToBeUpdated = showOneBookingById(bookingToBeUpdatedId);
        //update client
        clientService.updateClientById(bookingToBeUpdated.getClient().getId(), updatedBookingInfo.getClient());
        //update booking
        bookingToBeUpdated.setClient(bookingToBeUpdated.getClient());
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

    public Rider showOneRiderById(Long riderId) {
        return riderRepository.findById(riderId).orElseThrow(() ->
                new IllegalStateException("Rider with id = " + riderId + " not found!"));
    }

    public void setRiderAssignedEquipment(Long bookingToBeUpdatedId,
                                          Long riderToBeUpdatedId,
                                          RiderAssignedEquipment updatedRiderAssignedEquipment) {
        Booking booking = showOneBookingById(bookingToBeUpdatedId);
        Rider rider = showOneRiderById(riderToBeUpdatedId);

        BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(booking, rider);
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
        bookingRiderEquipmentLinkRepository.save(link);
    }

    public BookingRiderEquipmentLink getBookingRiderEquipmentLink(Booking booking, Rider rider) {
        return bookingRiderEquipmentLinkRepository.findByBookingIdAndRiderId(booking.getId(), rider.getId());
    }

    public void addRiderToBooking(Long bookingId, Long riderId) {
        Booking booking = showOneBookingById(bookingId);
        Rider rider = showOneRiderById(riderId);

        bookingRiderEquipmentLinkRepository
                .save(new BookingRiderEquipmentLink(booking, rider, new ArrayList<>(), new RiderAssignedEquipment()));
    }

    public void unassignRiderAssignedEquipment(BookingRiderEquipmentLink link) {
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
        bookingRiderEquipmentLinkRepository.save(link);
    }

    public void removeRiderFromBooking(Long bookingToBeUpdatedId, Long riderToBeRemovedId) {
        Booking booking = showOneBookingById(bookingToBeUpdatedId);
        Rider rider = showOneRiderById(riderToBeRemovedId);
        BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(booking, rider);

        unassignRiderAssignedEquipment(link);

        List<BookingRiderEquipmentLink> links = booking.getListOfBookingRiderEquipmentLinks();

        links.remove(link);
        bookingRiderEquipmentLinkRepository.delete(link);

        booking.setListOfBookingRiderEquipmentLinks(links);
        bookingRepository.save(booking);
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
        bookingRiderEquipmentLinkRepository.deleteAll(bookingToBeDeleted.getListOfBookingRiderEquipmentLinks());
        bookingRepository.delete(bookingToBeDeleted);
    }

    // ----- mark booking completed -----
    public void changeBookingCompleted(Long bookingId) {
        Booking booking = showOneBookingById(bookingId);
        booking.setCompleted(booking.isCompleted() ? false : true); //TODO: check
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

    // ----- ClientBookingController / add new booking -----
    public Client showCurrentClient() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return clientService.showOneClientByEmail(username);
    }

    // ----- ClientHomeController / show upcoming bookings for the client -----
    public List<Booking> showCurrentBookingsForClient(String email) {
        return bookingRepository.findAllByClientEmailAndDateOfReturnAfter(email, new Date()); //TODO: check result
    }

    // ----- ClientHomeController / update applicationUser info -----
    public void updateApplicationUserInfo(ApplicationUser applicationUserToBeUpdated, RegistrationRequest registrationRequest) {
        boolean emailExists = applicationUserService.checkIfExists(registrationRequest.getEmail());
        if (!emailExists || registrationRequest.getEmail().equals(applicationUserToBeUpdated.getEmail())) {
            applicationUserService.updateApplicationUserInfo(applicationUserToBeUpdated, registrationRequest);
            Client clientToBeUpdated = clientService.showOneClientByEmail(applicationUserToBeUpdated.getEmail());
            clientService.updateClientById(clientToBeUpdated.getId(), registrationRequest);
        }
    }

    public void updateApplicationUserPassword(ApplicationUser applicationUserToBeUpdated, String password) {
        applicationUserService.updateApplicationUserPassword(applicationUserToBeUpdated, password);
    }
}
