package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-booking")
public class BookingController<T extends Equipment> {

    private final BookingService<T> bookingService;

    @Autowired
    public BookingController(BookingService<T> bookingService) {
        this.bookingService = bookingService;
    }

    // ----- show all bookings -----
    @GetMapping()
    public String showAllBookings(Model model) {
        model.addAttribute("allBookings", bookingService.showAllBookings());
        return "admin/booking/show_all";
    }

    // ----- add new booking -----
    @GetMapping("/add-new")
    public String createNewBooking(Model model) {
        model.addAttribute("newBooking", new Booking());
        return "admin/booking/add_new";
    }

    @PostMapping("/add-new")
    public String addNewClientAndBookingToDB(@ModelAttribute("newBooking") @Valid Booking newBooking,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/booking/add_new";
        }
        bookingService.addNewBookingToDB(newBooking);
        return "redirect:/admin/info-riders/add-new?bookingId=" + newBooking.getId();
    }

    // ----- edit booking info -----
    @GetMapping("/edit/{bookingId}")
    public String showOneBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId, Model model) {
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);

        model.addAttribute("bookingToBeUpdated", bookingToBeUpdated);
        model.addAttribute("existingRiderToBeAddedId", 0L);
        model.addAttribute("allAvailableRidersForClient",
                bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));

        model.addAttribute("allAvailableSnowboards", bookingService.showAllAvailableSnowboards(bookingToBeUpdated));
        model.addAttribute("allAvailableSnowboardBoots", bookingService.showAllAvailableSnowboardBoots(bookingToBeUpdated));
        model.addAttribute("allAvailableSki", bookingService.showAllAvailableSki(bookingToBeUpdated));
        model.addAttribute("allAvailableSkiBoots", bookingService.showAllAvailableSkiBoots(bookingToBeUpdated));
        model.addAttribute("allAvailableJackets", bookingService.showAllAvailableJackets(bookingToBeUpdated));
        model.addAttribute("allAvailableKneeProtection", bookingService.showAllAvailableKneeProtection(bookingToBeUpdated));
        model.addAttribute("allAvailableProtectiveShorts", bookingService.showAllAvailableProtectiveShorts(bookingToBeUpdated));
        model.addAttribute("allAvailableGloves", bookingService.showAllAvailableGloves(bookingToBeUpdated));
        model.addAttribute("allAvailablePants", bookingService.showAllAvailablePants(bookingToBeUpdated));
        model.addAttribute("allAvailableHelmets", bookingService.showAllAvailableHelmets(bookingToBeUpdated));
        return "admin/booking/edit";
    }

    @PatchMapping("/edit/{bookingId}")
    public String updateBookingAndClientInfo(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                             @ModelAttribute("bookingToBeUpdated") @Valid Booking updatedBookingInfo,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            bookingService.setListOfRiders(updatedBookingInfo, bookingService.getListOfRiders(bookingToBeUpdatedId));
            model.addAttribute("bookingToBeUpdated", updatedBookingInfo);
            model.addAttribute("existingRiderToBeAddedId", 0L);
            model.addAttribute("allAvailableRidersForClient",
                    bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
            return "admin/booking/edit";
        }
        bookingService.updateBookingById(bookingToBeUpdatedId, updatedBookingInfo);
       return "redirect:/admin/info-booking/edit/{bookingId}";
    }

    @PatchMapping("/edit-equipment/{riderId}")
    public String updateRiderRequestedEquipment(@PathVariable("riderId") Long riderToBeUpdatedId,
                                                @RequestParam("bookingId") Long bookingToBeUpdatedId,
                                                @ModelAttribute("link") BookingRiderEquipmentLink updatedLink) {
        bookingService.updateRiderRequestedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, updatedLink);
        return "redirect:/admin/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/assign-equipment")
    public String assignEquipmentToOneRider(@RequestParam("bookingId") Long bookingToBeUpdatedId,
                                            @RequestParam("riderId") Long riderToBeUpdatedId,
                                            @ModelAttribute("oneBookingRiderEquipmentLink.assignedEquipment") RiderAssignedEquipment riderAssignedEquipment) {
        bookingService.setRiderAssignedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, riderAssignedEquipment);
        return "redirect:/admin/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @GetMapping("/edit/remove-rider")
    public String removeRiderFromBooking(@RequestParam("bookingId") Long bookingToBeUpdatedId,
                                         @RequestParam("riderId") Long riderToBeRemovedId) {
        bookingService.removeRiderFromBooking(bookingToBeUpdatedId, riderToBeRemovedId);
        return "redirect:/admin/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/add-existing-rider/{bookingId}")
    public String addExistingRiderToBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                            @ModelAttribute("existingRiderToBeAddedId") Long existingRiderToBeAddedId) {
        bookingService.addRiderToBooking(bookingToBeUpdatedId, existingRiderToBeAddedId);
        return "redirect:/admin/info-booking/edit/" + bookingToBeUpdatedId;
    }

    // ----- delete booking -----
    @DeleteMapping("/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.deleteBookingById(bookingId);
        return "redirect:/admin/info-booking";
    }

    // ----- mark booking completed -----
    @GetMapping("/change-booking-completed/{bookingId}")
    public String changeBookingCompleted(@PathVariable("bookingId") Long bookingId) {
        bookingService.changeBookingCompleted(bookingId);
        return "redirect:/admin/info-booking";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showBookingsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("bookingsBySearch", bookingService.showBookingsBySearch(search));
        model.addAttribute("search", search);
        return "admin/booking/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortBookingsByParameter(@RequestParam("parameter") String parameter,
                                          @RequestParam("sortDirection") String sortDirection,
                                          Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allBookings", bookingService.sortAllBookingsByParameter(parameter, sortDirection));
        return "admin/booking/show_all";
    }
}
