package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientHomeController {

    private final BookingService bookingService;

    @Autowired
    public ClientHomeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ModelAttribute
    public void addToModel(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated, Model model) {
        model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
    }

    // ----- client home page -----
    @GetMapping
    public String showClientMainPage(Model model) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long clientId = applicationUser.getClient().getId();
        model.addAttribute("currentBookingsForClient", bookingService.showCurrentBookingsForClient(clientId));
        return "client/home/main_page";
    }
}
