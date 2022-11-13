package my.project.skirentalshop.entity;

import lombok.*;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Client {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @NotEmpty(message = "{validation.client.invalid-name}")
    private String name;

    @Pattern(regexp = "(\\d\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2})", message = "{validation.client.invalid-phone-number}")
    private String phone1;

    @Pattern(regexp = "(\\d\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2})|(^$)", message = "{validation.client.invalid-phone-number}")
    private String phone2;

    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    @ToString.Exclude
    private List<Booking> listOfBookings;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL) //TODO: check
    @ToString.Exclude
    private ApplicationUser applicationUser;

    public Client(String name, String phone1, String phone2) {
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }
}
