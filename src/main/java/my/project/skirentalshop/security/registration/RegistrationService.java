package my.project.skirentalshop.security.registration;

import my.project.skirentalshop.entity.Client;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public RegistrationService(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    public void register(RegistrationRequest registrationRequest) {
        Client newClient = new Client(
                registrationRequest.getName(),
                registrationRequest.getPhone1(),
                null
        );

        applicationUserService.signUpUser(new ApplicationUser(
                newClient,
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                ApplicationUserRole.CLIENT
        ));
    }
}
