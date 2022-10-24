package my.project.skirentalshop.security.registration;

import my.project.skirentalshop.model.Client;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import my.project.skirentalshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final ApplicationUserService applicationUserService;
    private final ClientService clientService;

    @Autowired
    public RegistrationService(ApplicationUserService applicationUserService,
                               ClientService clientService) {
        this.applicationUserService = applicationUserService;
        this.clientService = clientService;
    }

    public void register(RegistrationRequest registrationRequest) {
        applicationUserService.signUpUser(new ApplicationUser(
                registrationRequest.getName(),
                registrationRequest.getSurname(),
                registrationRequest.getPhone1(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                ApplicationUserRole.CLIENT
        ));

        clientService.addNewClientToDB(new Client(
                registrationRequest.getName() + " " + registrationRequest.getSurname(),
                registrationRequest.getPhone1(),
                null,
                registrationRequest.getEmail()));
    }
}
