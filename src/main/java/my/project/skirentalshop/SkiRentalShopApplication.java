package my.project.skirentalshop;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SkiRentalShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkiRentalShopApplication.class, args);
    }

    @Autowired //TODO: resolve
    ApplicationUserService applicationUserService;

    @Bean
    public CommandLineRunner run() {
        return args -> {
            try {
                ApplicationUser admin = new ApplicationUser(
                        "", "", "", "admin", "admin", ApplicationUserRole.ADMIN);
                applicationUserService.signUpUser(admin);
                System.out.println("ADMIN has been created!");
            } catch (IllegalStateException e) {
                System.out.println("ADMIN already exists!");
            }
        };
    }
}
