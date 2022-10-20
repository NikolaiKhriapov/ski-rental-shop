package my.project.skirentalshop.security;

import my.project.skirentalshop.model.Client;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import my.project.skirentalshop.security.registration.RegistrationService;
import my.project.skirentalshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final RegistrationService registrationService;
    private final ClientService clientService;

    @Autowired
    public AuthenticationController(RegistrationService registrationService, ClientService clientService) {
        this.registrationService = registrationService;
        this.clientService = clientService;
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "authentication/sign_in";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "authentication/sign_up";
    }

    @PostMapping("/sign-up")
    public String addNewUserToDB(@ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authentication/sign_up";
        }
        registrationService.register(registrationRequest);

        Client newClient = new Client(registrationRequest.getName() + " " + registrationRequest.getSurname(),
                registrationRequest.getPhone1(), null);
        newClient.setEmail(registrationRequest.getEmail());
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
