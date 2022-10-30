package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.*;

@Controller
@RequestMapping("/admin/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllBookings(Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfBookings", bookingService.showAllBookings());
        return "admin/booking/bookings";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewBooking(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("booking", new Booking());
        return "admin/booking/bookings";
    }

    @PostMapping("/add-new")
    public String addNewBookingToDB(@ModelAttribute("booking") @Valid Booking newBooking,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "admin/booking/bookings";
        }
        bookingService.addNewBookingToDB(newBooking);
        return "redirect:/admin/riders/add-new?bookingId=" + newBooking.getId();
    }

    // ----- edit -----
    @GetMapping("/edit/{bookingId}")
    public String showOneBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId, Model model) {
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);

        model.addAttribute("action", "update");
        model.addAttribute("booking", bookingToBeUpdated);
        model.addAttribute("existingRiderToBeAddedId", 0L);
        model.addAttribute("allAvailableRidersForClient",
                bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));

        model.addAttribute("allAvailableSnowboards", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, SNOWBOARD));
        model.addAttribute("allAvailableSnowboardBoots", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, SNOWBOARD_BOOTS));
        model.addAttribute("allAvailableSki", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, SKI));
        model.addAttribute("allAvailableSkiBoots", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, SKI_BOOTS));
        model.addAttribute("allAvailableHelmets", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, HELMET));
        model.addAttribute("allAvailableJackets", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, JACKET));
        model.addAttribute("allAvailableGloves", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, GLOVES));
        model.addAttribute("allAvailablePants", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, PANTS));
        model.addAttribute("allAvailableProtectiveShorts", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, PROTECTIVE_SHORTS));
        model.addAttribute("allAvailableKneeProtection", bookingService.showAllAvailableEquipmentByType(bookingToBeUpdated, KNEE_PROTECTION));
        return "admin/booking/bookings";
    }

    @PatchMapping("/edit/{bookingId}")
    public String updateBookingAndClientInfo(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                             @ModelAttribute("booking") @Valid Booking updatedBookingInfo,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            bookingService.setListOfRiders(updatedBookingInfo, bookingService.getListOfRiders(bookingToBeUpdatedId));
            model.addAttribute("action", "update");
            model.addAttribute("booking", updatedBookingInfo);
            model.addAttribute("existingRiderToBeAddedId", 0L);
            model.addAttribute("allAvailableRidersForClient",
                    bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
            return "admin/booking/bookings";
        }
        bookingService.updateBookingById(bookingToBeUpdatedId, updatedBookingInfo);
        return "redirect:/admin/bookings/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit-equipment/{riderId}")
    public String updateRiderRequestedEquipment(@PathVariable("riderId") Long riderToBeUpdatedId,
                                                @RequestParam("bookingId") Long bookingToBeUpdatedId,
                                                @ModelAttribute("link") BookingRiderEquipmentLink updatedLink) {
        bookingService.updateRiderRequestedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, updatedLink);
        return "redirect:/admin/bookings/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/assign-equipment")
    public String assignEquipmentToOneRider(@RequestParam("bookingId") Long bookingToBeUpdatedId,
                                            @RequestParam("riderId") Long riderToBeUpdatedId,
                                            @ModelAttribute("oneBookingRiderEquipmentLink.assignedEquipment") RiderAssignedEquipment riderAssignedEquipment) {
        bookingService.setRiderAssignedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, riderAssignedEquipment);
        return "redirect:/admin/bookings/edit/" + bookingToBeUpdatedId;
    }

    @GetMapping("/edit/remove-rider")
    public String removeRiderFromBooking(@RequestParam("bookingId") Long bookingToBeUpdatedId,
                                         @RequestParam("riderId") Long riderToBeRemovedId) {
        bookingService.removeRiderFromBooking(bookingToBeUpdatedId, riderToBeRemovedId);
        return "redirect:/admin/bookings/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/add-existing-rider/{bookingId}")
    public String addExistingRiderToBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                            @ModelAttribute("existingRiderToBeAddedId") Long existingRiderToBeAddedId) {
        bookingService.addRiderToBooking(bookingToBeUpdatedId, existingRiderToBeAddedId);
        return "redirect:/admin/bookings/edit/" + bookingToBeUpdatedId;
    }

    // ----- delete -----
    @DeleteMapping("/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.deleteBookingById(bookingId);
        return "redirect:/admin/bookings";
    }

    // ----- mark completed -----
    @GetMapping("/change-booking-completed/{bookingId}")
    public String changeBookingCompleted(@PathVariable("bookingId") Long bookingId) {
        bookingService.changeBookingCompleted(bookingId);
        return "redirect:/admin/bookings";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showBookingsBySearch(@RequestParam("search") String search,
                                       Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfBookings", bookingService.showBookingsBySearch(search));
        model.addAttribute("search", search);
        return "admin/booking/bookings";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortBookingsByParameter(@RequestParam("parameter") String parameter,
                                          @RequestParam("sortDirection") String sortDirection,
                                          Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfBookings",
                bookingService.sortAllBookingsByParameter(parameter, sortDirection));
        return "admin/booking/bookings";
    }
}
