package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/riders")
public class RiderController {

    private final RiderService riderService;

    @Autowired
    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllRiders(Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfRiders", riderService.showAllRiders());
        model.addAttribute("listOfBookings", riderService.showAllBookings());
        return "admin/rider/riders";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewRider(@RequestParam(value = "bookingId", required = false) Long bookingId,
                                 Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("rider", new Rider());
        model.addAttribute("bookingId", bookingId);
        return "admin/rider/riders";
    }

    @PostMapping("/add-new")
    public String addNewRiderToDB(@RequestParam(value = "bookingId", required = false) Long bookingId,
                                  @ModelAttribute("rider") @Valid Rider rider,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "admin/rider/riders";
        }
        riderService.addNewRiderToDB(rider);

        if (bookingId != null) {
            riderService.addRiderToBooking(bookingId, rider);
            return "redirect:/admin/riders/add-new?bookingId=" + bookingId;
        }
        return "redirect:/admin/riders/";
    }

    // ----- edit -----
    @GetMapping("/edit")
    public String showOneRider(@RequestParam("riderId") Long riderId,
                               @RequestParam(value = "bookingId", required = false) Long bookingId,
                               Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("rider", riderService.showOneRiderById(riderId));
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("riderId", riderId);
        if (bookingId != null) {
            model.addAttribute("link", riderService.getBookingRiderEquipmentLink(
                    riderService.showOneBookingById(bookingId), riderService.showOneRiderById(riderId)));
        }
        return "admin/rider/riders";
    }

    @PatchMapping("/edit/{riderId}")
    public String updateRider(@PathVariable("riderId") Long riderToBeUpdatedId,
                              @RequestParam(value = "bookingId", required = false) Long bookingId,
                              @ModelAttribute("rider") @Valid Rider updatedRider,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            model.addAttribute("riderId", riderToBeUpdatedId);
            model.addAttribute("bookingId", bookingId);
            if (bookingId != null) {
                model.addAttribute("link", riderService.getBookingRiderEquipmentLink(
                        riderService.showOneBookingById(bookingId), riderService.showOneRiderById(riderToBeUpdatedId)));
            }
            return "admin/rider/riders";
        }
        riderService.updateRiderById(riderToBeUpdatedId, updatedRider);

        if (bookingId != null) {
            return "redirect:/admin/bookings/edit/" + bookingId;
        } else {
            return "redirect:/admin/riders";
        }
    }

    // ----- delete -----
    @DeleteMapping("/{riderId}")
    public String deleteRider(@PathVariable("riderId") Long riderId) {
        riderService.deleteRiderById(riderId);
        return "redirect:/admin/riders";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllRidersBySearch(@RequestParam("search") String search,
                                        Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfRiders", riderService.showRidersBySearch(search));
        model.addAttribute("search", search);
        return "admin/rider/riders";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortRidersByParameter(@RequestParam("parameter") String parameter,
                                        @RequestParam("sortDirection") String sortDirection,
                                        Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfRiders", riderService.sortAllRidersByParameter(parameter, sortDirection));
        model.addAttribute("listOfBookings", riderService.showAllBookings());
        return "admin/rider/riders";
    }
}
