package my.project.skirentalshop.entity;

import lombok.*;
import my.project.skirentalshop.validation.BookingDates;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@NoArgsConstructor
@Data
@BookingDates
public class Booking {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    private Client client;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "{validation.booking.date.not-null}")
    @Future(message = "{validation.booking.date.future}")
    private Date dateOfArrival;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "{validation.booking.date.not-null}")
    @Future(message = "{validation.booking.date.future}")
    private Date dateOfReturn;

    private boolean completed;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;

    public Booking(Client client, Date dateOfArrival, Date dateOfReturn) {
        this.client = client;
        this.dateOfArrival = dateOfArrival;
        this.dateOfReturn = dateOfReturn;
    }
}
