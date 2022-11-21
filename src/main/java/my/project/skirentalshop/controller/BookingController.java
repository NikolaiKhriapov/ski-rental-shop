package my.project.skirentalshop.controller;

import my.project.skirentalshop.dto.RiderAssignedEquipmentDTO;
import my.project.skirentalshop.entity.*;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{applicationUserRole}/bookings")
@SuppressWarnings("SpringMVCViewInspection")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ----- show all -----
    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfBookings", bookingService.showAllBookings());
        return "bookings";
    }

    // ----- add new -----
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("booking", bookingService.createNewBooking());
        return "bookings";
    }

    @PostMapping
    public String create(@PathVariable("applicationUserRole") String applicationUserRole,
                         @ModelAttribute("booking") @Valid Booking newBooking,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "bookings";
        }
        bookingService.addNewBookingToDB(newBooking);
        return "redirect:/" + applicationUserRole + "/riders/new?bookingId=" + newBooking.getId();
    }

    // ----- edit -----
    @GetMapping("/{bookingId}")
    public String showOne(@PathVariable("bookingId") Long bookingToBeUpdatedId, Model model) {
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);

        model.addAttribute("action", "update");
        model.addAttribute("booking", bookingToBeUpdated);
        model.addAttribute("bookingService", bookingService);
        model.addAttribute("riderAssignedEquipmentDTO", new RiderAssignedEquipmentDTO());
        model.addAttribute("existingRiderToBeAddedId", 0L);
        model.addAttribute("allAvailableRidersForClient",
                bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
        return "bookings";
    }

    @PatchMapping("/{bookingId}")
    public String updateBookingAndClientInfo(@PathVariable("applicationUserRole") String applicationUserRole,
                                             @PathVariable("bookingId") Long bookingToBeUpdatedId,
                                             @ModelAttribute("booking") @Valid Booking updatedBookingInfo,
                                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bookingService.resetListOfRiders(updatedBookingInfo, bookingToBeUpdatedId);
            model.addAttribute("action", "update");
            model.addAttribute("bookingService", bookingService);
            model.addAttribute("booking", updatedBookingInfo);
            model.addAttribute("riderAssignedEquipmentDTO", new RiderAssignedEquipmentDTO());
            model.addAttribute("existingRiderToBeAddedId", 0L);
            model.addAttribute("allAvailableRidersForClient",
                    bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
            return "bookings";
        }
        bookingService.updateBookingById(bookingToBeUpdatedId, updatedBookingInfo);
        return "redirect:/" + applicationUserRole + "/bookings/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/{bookingId}/rider-requested-equipment/{riderId}")
    public String updateRiderRequestedEquipment(@PathVariable("applicationUserRole") String applicationUserRole,
                                                @PathVariable("bookingId") Long bookingToBeUpdatedId,
                                                @PathVariable("riderId") Long riderToBeUpdatedId,
                                                @ModelAttribute("link") BookingRiderEquipmentLink updatedLink) {
        bookingService.updateRiderRequestedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, updatedLink);
        return "redirect:/" + applicationUserRole + "/riders/" + riderToBeUpdatedId + "?bookingId=" + bookingToBeUpdatedId;
    }

    @PatchMapping("/{bookingId}/rider-assigned-equipment/{riderId}")
    public String updateRiderAssignedEquipment(@PathVariable("applicationUserRole") String applicationUserRole,
                                               @PathVariable("bookingId") Long bookingToBeUpdatedId,
                                               @PathVariable("riderId") Long riderToBeUpdatedId,
                                               @ModelAttribute("riderAssignedEquipmentDTO")
                                                   RiderAssignedEquipmentDTO riderAssignedEquipmentDTO) {
        bookingService.updateRiderAssignedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, riderAssignedEquipmentDTO);
        return "redirect:/" + applicationUserRole + "/bookings/" + bookingToBeUpdatedId;
    }

    @GetMapping("/{bookingId}/remove-rider/{riderId}")
    public String removeRiderFromBooking(@PathVariable("applicationUserRole") String applicationUserRole,
                                         @PathVariable("bookingId") Long bookingToBeUpdatedId,
                                         @PathVariable("riderId") Long riderToBeRemovedId) {
        bookingService.removeRiderFromBooking(bookingToBeUpdatedId, riderToBeRemovedId);
        return "redirect:/" + applicationUserRole + "/bookings/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/{bookingId}/new-existing-rider")
    public String addExistingRiderToBooking(@PathVariable("applicationUserRole") String applicationUserRole,
                                            @PathVariable("bookingId") Long bookingToBeUpdatedId,
                                            @ModelAttribute("existingRiderToBeAddedId") Long existingRiderToBeAddedId) {
        bookingService.addExistingRiderToBooking(bookingToBeUpdatedId, existingRiderToBeAddedId);
        return "redirect:/" + applicationUserRole + "/bookings/" + bookingToBeUpdatedId;
    }

    // ----- delete -----
    @DeleteMapping("/{bookingId}")
    public String delete(@PathVariable("applicationUserRole") String applicationUserRole,
                         @PathVariable("bookingId") Long bookingId) {
        bookingService.deleteBookingById(bookingId);

        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        switch (user.getApplicationUserRole()) {
            case ADMIN -> {
                return "redirect:/admin/bookings";
            }
            case CLIENT -> {
                return "redirect:/client";
            }
            default -> {
                return "redirect:/" + applicationUserRole;
            }
        }
    }

    // ----- mark completed -----
    @GetMapping("/{bookingId}/change-completed")
    public String changeCompleted(@PathVariable("applicationUserRole") String applicationUserRole,
                                  @PathVariable("bookingId") Long bookingId) {
        bookingService.changeBookingCompleted(bookingId);
        return "redirect:/" + applicationUserRole + "/bookings";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showBySearch(@RequestParam("search") String search,
                               Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfBookings", bookingService.showBookingsBySearch(search));
        model.addAttribute("search", search);
        return "bookings";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortByParameter(@RequestParam("parameter") String parameter,
                                  @RequestParam("sortDirection") String sortDirection,
                                  Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfBookings",
                bookingService.sortAllBookingsByParameter(parameter, sortDirection));
        return "bookings";
    }
}
