package my.project.skirentalshop.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Booking {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @Valid
    private Client client;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "{validation.booking.invalid-date}")
    private Date dateOfArrival;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "{validation.booking.invalid-date}")
    private Date dateOfReturn;

    private boolean completed;

    @OneToMany(mappedBy = "booking")
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;


    public Booking(Client client, Date dateOfArrival, Date dateOfReturn) {
        this.client = client;
        this.dateOfArrival = dateOfArrival;
        this.dateOfReturn = dateOfReturn;
    }
}
