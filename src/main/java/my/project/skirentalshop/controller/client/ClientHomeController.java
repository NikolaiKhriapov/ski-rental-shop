package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.model.Client;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import my.project.skirentalshop.service.BookingService;
import my.project.skirentalshop.service.ClientService;
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
    private final ClientService clientService;

    public ClientHomeController(BookingService bookingService,
                                ApplicationUserService applicationUserService,
                                ClientService clientService) {
        this.bookingService = bookingService;
        this.applicationUserService = applicationUserService;
        this.clientService = clientService;
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
    public String showSettings(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated, Model model) {
        model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "client/home/settings";
    }

    @PatchMapping("/settings/edit-info")
    public String updateApplicationUserInfo(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                            @ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
            return "client/home/settings";
        }
        boolean emailExists = applicationUserService.checkIfExists(registrationRequest.getEmail());
        if (!emailExists || registrationRequest.getEmail().equals(applicationUserToBeUpdated.getEmail())) {
            applicationUserService.updateApplicationUserInfo(applicationUserToBeUpdated, registrationRequest);
            Client clientToBeUpdated = clientService.showOneClientByEmail(applicationUserToBeUpdated.getEmail());
            clientService.updateUserInfo(clientToBeUpdated, registrationRequest);
        }
        return "redirect:/client/settings";
    }

    @PatchMapping("/settings/edit-password")
    public String updateApplicationUserPassword(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                                @ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
            return "client/home/settings";
        }
        applicationUserService.updateApplicationUserPassword(applicationUserToBeUpdated, registrationRequest.getPassword());
        return "redirect:/client/settings";
    }
}
