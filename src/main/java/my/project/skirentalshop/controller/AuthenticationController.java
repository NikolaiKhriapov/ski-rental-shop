package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Client;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import my.project.skirentalshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final ApplicationUserService applicationUserService;
    private final ClientService clientService;

    @Autowired
    public AuthenticationController(ApplicationUserService applicationUserService, ClientService clientService) {
        this.applicationUserService = applicationUserService;
        this.clientService = clientService;
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "authentication/sign_in";
    }

    @GetMapping("/sign-up")
    public String registerNewUser(Model model) {
        model.addAttribute("newApplicationUser", new ApplicationUser());
        return "authentication/sign_up";
    }

    @PostMapping("/sign-up")
    public String addNewUserToDB(@ModelAttribute("newApplicationUser") @Valid ApplicationUser newApplicationUser,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authentication/sign_up";
        }
        applicationUserService.signUpUser(newApplicationUser);
        Client newClient = new Client(newApplicationUser.getName() + " " + newApplicationUser.getSurname(),
                newApplicationUser.getPhone(), null);
        newClient.setEmail(newApplicationUser.getEmail());
        clientService.addNewClientToDB(newClient);
        return "redirect:/sign-in";
    }

    @RequestMapping("/home")
    public String defaultAfterLogin(Authentication authResult) {
        String role = authResult.getAuthorities().toString();
        if (role.equals("[ADMIN]")) {
            return "redirect:/admin";
        } else {
            return "redirect:/client";
        }
    }
}
