package my.project.skirentalshop.controller;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import my.project.skirentalshop.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/client")
public class ClientHomeController {

    private final BookingService bookingService;

    @Autowired
    public ClientHomeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ----- client home page -----
    @GetMapping
    public String showClientMainPage(Model model) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long clientId = applicationUser.getClient().getId();
        model.addAttribute("currentBookingsForClient", bookingService.showCurrentBookingsForClient(clientId));
        return "client/home/main_page";
    }

    // ----- update applicationUser info -----
    @GetMapping("/settings")
    public String showSettings(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                               Model model) {
        model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "client/home/settings";
    }

    @PatchMapping("/settings/personal-info")
    public String updateApplicationUserInfo(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                            @ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                            BindingResult bindingResult, Model model)    {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
            return "client/home/settings";
        }
        bookingService.updateApplicationUserInfo(applicationUserToBeUpdated, registrationRequest);
        return "redirect:/client/settings";
    }

    @PatchMapping("/settings/password")
    public String updateApplicationUserPassword(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                                @ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
            return "client/home/settings";
        }
        bookingService.updateApplicationUserPassword(applicationUserToBeUpdated, registrationRequest.getPassword());
        return "redirect:/client/settings";
    }
}
