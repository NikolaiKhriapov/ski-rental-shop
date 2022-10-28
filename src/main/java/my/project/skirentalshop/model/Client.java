package my.project.skirentalshop.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Client {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @NotEmpty(message = "{validation.client.invalid_name}")
    private String name;

    @Pattern(regexp = "[\\d]\\([\\d]{3}\\)[\\d]{3}-[\\d]{2}-[\\d]{2}", message = "{validation.client.invalid_phone_number}")
    private String phone1;

    private String phone2;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.client.invalid_email}")
    private String email;

    @OneToMany(mappedBy = "client")
    private List<Booking> listOfBookings;

    public Client(String name, String phone1, String phone2) {
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public Client(String name, String phone1, String phone2, String email) {
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
    }
}
