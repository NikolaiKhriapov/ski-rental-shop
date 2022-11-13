package my.project.skirentalshop.service;

import my.project.skirentalshop.entity.*;
import my.project.skirentalshop.entity.enums.EquipmentCondition;
import my.project.skirentalshop.entity.enums.TypesOfEquipment;
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
                    listOfAvailableEquipment.remove(getEquipmentByType(link.getRiderAssignedEquipment(), typeOfEquipment));
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
                                          RiderAssignedEquipmentDTO riderAssignedEquipmentDTO) {
        List<Equipment> riderAssignedEquipment = new ArrayList<>();
        if (riderAssignedEquipmentDTO.getSnowboard().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSnowboard());
        }
        if (riderAssignedEquipmentDTO.getSnowboardBoots().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSnowboardBoots());
        }
        if (riderAssignedEquipmentDTO.getSki().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSki());
        }
        if (riderAssignedEquipmentDTO.getSkiBoots().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getSkiBoots());
        }
        if (riderAssignedEquipmentDTO.getHelmet().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getHelmet());
        }
        if (riderAssignedEquipmentDTO.getJacket().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getJacket());
        }
        if (riderAssignedEquipmentDTO.getGloves().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getGloves());
        }
        if (riderAssignedEquipmentDTO.getPants().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getPants());
        }
        if (riderAssignedEquipmentDTO.getProtectiveShorts().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getProtectiveShorts());
        }
        if (riderAssignedEquipmentDTO.getKneeProtection().getId() != null) {
            riderAssignedEquipment.add(riderAssignedEquipmentDTO.getKneeProtection());
        }

        Booking booking = showOneBookingById(bookingToBeUpdatedId);
        getBookingRiderEquipmentLink(booking, riderToBeUpdatedId).setRiderAssignedEquipment(riderAssignedEquipment);
        bookingRepository.save(booking);
    }

    public boolean containsEquipmentByType(List<Equipment> listOfEquipment, TypesOfEquipment type) {
        return listOfEquipment.stream().anyMatch(oneEquipment -> oneEquipment.getType() == type);
    }

    public Equipment getEquipmentByType(List<Equipment> listOfEquipment, TypesOfEquipment type) {
        List<Equipment> equipment = listOfEquipment.stream()
                .filter(oneEquipment -> oneEquipment.getType() == type)
                .toList();

        if (equipment.size() > 1) {
            throw new IllegalStateException("More than 2 pieces of equipment of type " + type + " have been chosen!");
        } else if (equipment.size() < 1) {
            return null;
        } else {
            return equipment.get(0);
        }
    }

    public void removeRiderFromBooking(Long bookingToBeUpdatedId, Long riderToBeRemovedId) {
        Booking booking = showOneBookingById(bookingToBeUpdatedId);
        BookingRiderEquipmentLink link = getBookingRiderEquipmentLink(booking, riderToBeRemovedId);

        link.setRiderAssignedEquipment(new ArrayList<>());
        link.setBooking(null);
        link.setRider(null);

        booking.getListOfBookingRiderEquipmentLinks().remove(link);

        bookingRepository.save(booking);
    }

    public void addExistingRiderToBooking(Long bookingId, Long riderId) {
        Booking booking = showOneBookingById(bookingId);
        Rider rider = showOneRiderById(riderId);

        booking.getListOfBookingRiderEquipmentLinks()
                .add(new BookingRiderEquipmentLink(booking, rider, new ArrayList<>(), new ArrayList<>()));
        bookingRepository.save(booking);
    }

    public Rider showOneRiderById(Long id) {
        return riderRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Rider with id = " + id + " not found!"));
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
