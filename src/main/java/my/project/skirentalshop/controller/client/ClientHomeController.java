package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import my.project.skirentalshop.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/client")
public class ClientHomeController {

    private final BookingService bookingService;
    private final ApplicationUserService applicationUserService;

    public ClientHomeController(BookingService bookingService, ApplicationUserService applicationUserService) {
        this.bookingService = bookingService;
        this.applicationUserService = applicationUserService;
    }

    // ----- client home page -----
    @GetMapping()
    public String showClientMainPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentBookingsForClient", bookingService.showCurrentBookingsForClient(username));
        return "client/home/main_page";
    }

    // ----- client history -----
    @GetMapping("/history")
    public String showClientHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("allBookingsForClient", bookingService.showAllBookingsForClient(username));
        return "client/home/history";
    }

    // ----- update applicationUser info -----
    @GetMapping("/settings")
    public String showSettings(@AuthenticationPrincipal ApplicationUser applicationUser, Model model) {
        model.addAttribute("applicationUser", applicationUser);
        return "client/home/settings";
    }

    @PatchMapping("/settings/edit-info")
    public String updateApplicationUserInfo(@AuthenticationPrincipal ApplicationUser applicationUserToUpdate,
                                            @ModelAttribute("applicationUser") @Valid ApplicationUser updatedApplicationUser,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/home/settings";
        }
        applicationUserService.changeApplicationUserInfo(applicationUserToUpdate, updatedApplicationUser);
        return "redirect:/client/settings";
    }

    @PatchMapping("/settings/edit-password")
    public String updateApplicationUserPassword(@AuthenticationPrincipal ApplicationUser applicationUserToUpdate,
                                                @ModelAttribute("applicationUser") @Valid ApplicationUser updatedApplicationUser,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/home/settings";
        }
        applicationUserService.changeApplicationUserPassword(applicationUserToUpdate, updatedApplicationUser.getPassword());
        return "redirect:/client/settings";
    }
}
