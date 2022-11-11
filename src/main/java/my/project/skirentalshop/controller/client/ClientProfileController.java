package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/client/settings")
public class ClientProfileController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public ClientProfileController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    // ----- update applicationUser info -----
    @GetMapping
    public String showSettings(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                               Model model) {
        model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "client/home/settings";
    }

    @PatchMapping("/personal-info")
    public String updateApplicationUserInfo(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                            @ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
            return "client/home/settings";
        }
        applicationUserService.updatePersonalInfo(applicationUserToBeUpdated, registrationRequest);
        return "redirect:/client/settings";
    }

    @PatchMapping("/password")
    public String updateApplicationUserPassword(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                                @ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicationUserToBeUpdated", applicationUserToBeUpdated);
            return "client/home/settings";
        }
        applicationUserService.updatePassword(applicationUserToBeUpdated, registrationRequest.getPassword());
        return "redirect:/client/settings";
    }

    @PatchMapping("/photo")
    public String updateApplicationUserPhoto(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated,
                                             @RequestParam("photo") MultipartFile file) throws IOException {
        applicationUserService.updatePhoto(applicationUserToBeUpdated, file);
        return "redirect:/client/settings";
    }

    @GetMapping("/photo-delete")
    public String deleteApplicationUserPhoto(@AuthenticationPrincipal ApplicationUser applicationUserToBeUpdated) throws IOException {
        applicationUserService.deletePhoto(applicationUserToBeUpdated);
        return "redirect:/client/settings";
    }
}
