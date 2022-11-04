package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{applicationUserRole}/riders")
@SuppressWarnings("SpringMVCViewInspection")
public class RiderController {

    private final RiderService riderService;

    @Autowired
    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    // ----- show all -----
    @GetMapping
    public String showAll(@PathVariable("applicationUserRole") String applicationUserRole,
                          Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfRiders", riderService.showAllRiders());
        return applicationUserRole + "/rider/riders";
    }

    // ----- add new -----
    @GetMapping("/new")
    public String create(@PathVariable("applicationUserRole") String applicationUserRole,
                         @RequestParam(value = "bookingId", required = false) Long bookingId,
                         Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("rider", new Rider());
        model.addAttribute("bookingId", bookingId);
        return applicationUserRole + "/rider/riders";
    }

    @PostMapping
    public String create(@PathVariable("applicationUserRole") String applicationUserRole,
                         @RequestParam(value = "bookingId", required = false) Long bookingId,
                         @ModelAttribute("rider") @Valid Rider rider,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return applicationUserRole + "/rider/riders";
        }
        riderService.addNewRiderToDB(rider, bookingId);

        if (bookingId == null) {
            return "redirect:/" + applicationUserRole + "/riders/";
        } else {
            return "redirect:/" + applicationUserRole + "/riders/new?bookingId=" + bookingId;
        }
    }

    // ----- edit -----
    @GetMapping("/{riderId}")
    public String showOne(@PathVariable("applicationUserRole") String applicationUserRole,
                          @PathVariable("riderId") Long riderId,
                          @RequestParam(value = "bookingId", required = false) Long bookingId,
                          Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("rider", riderService.showOneRiderById(riderId));
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("riderId", riderId);
        model.addAttribute("link", riderService.getBookingRiderEquipmentLink(bookingId, riderId));
        return applicationUserRole + "/rider/riders";
    }

    @PatchMapping("/{riderId}")
    public String update(@PathVariable("applicationUserRole") String applicationUserRole,
                         @PathVariable("riderId") Long riderId,
                         @RequestParam(value = "bookingId", required = false) Long bookingId,
                         @ModelAttribute("rider") @Valid Rider updatedRider,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            model.addAttribute("riderId", riderId);
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("link", riderService.getBookingRiderEquipmentLink(bookingId, riderId));
            return applicationUserRole + "/rider/riders";
        }
        riderService.updateRiderById(riderId, updatedRider);

        if (bookingId == null) {
            return "redirect:/" + applicationUserRole + "/riders";
        } else {
            return "redirect:/" + applicationUserRole + "/bookings/" + bookingId;
        }
    }

    // ----- delete -----
    @DeleteMapping("/{riderId}")
    public String delete(@PathVariable("applicationUserRole") String applicationUserRole,
                         @PathVariable("riderId") Long riderId) {
        riderService.deleteRiderById(riderId);
        return "redirect:/" + applicationUserRole + "/riders";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllBySearch(@PathVariable("applicationUserRole") String applicationUserRole,
                                  @RequestParam("search") String search,
                                  Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfRiders", riderService.showRidersBySearch(search));
        model.addAttribute("search", search);
        return applicationUserRole + "/rider/riders";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllByParameter(@PathVariable("applicationUserRole") String applicationUserRole,
                                     @RequestParam("parameter") String parameter,
                                     @RequestParam("sortDirection") String sortDirection,
                                     Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfRiders", riderService.sortAllRidersByParameter(parameter, sortDirection));
        model.addAttribute("listOfBookings", riderService.showAllBookings());
        return applicationUserRole + "/rider/riders";
    }
}
