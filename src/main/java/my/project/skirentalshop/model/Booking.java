package my.project.skirentalshop.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
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
    @NotNull(message = "{validation.booking.invalid_date}")
    private Date dateOfArrival;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "{validation.booking.invalid_date}")
    private Date dateOfReturn;

    private boolean completed;

    @OneToMany(mappedBy = "booking")
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;

    public Booking() {
    }

    public Booking(Client client, Date dateOfArrival, Date dateOfReturn) {
        this.client = client;
        this.dateOfArrival = dateOfArrival;
        this.dateOfReturn = dateOfReturn;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<BookingRiderEquipmentLink> getListOfBookingRiderEquipmentLinks() {
        if (listOfBookingRiderEquipmentLinks == null) {
            return new ArrayList<>();
        }
        return listOfBookingRiderEquipmentLinks;
    }

    public void setListOfBookingRiderEquipmentLinks(List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks) {
        this.listOfBookingRiderEquipmentLinks = listOfBookingRiderEquipmentLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return completed == booking.completed &&
                Objects.equals(id, booking.id) &&
                Objects.equals(client, booking.client) &&
                Objects.equals(dateOfArrival, booking.dateOfArrival) &&
                Objects.equals(dateOfReturn, booking.dateOfReturn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, dateOfArrival, dateOfReturn, completed);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", client=" + client +
                ", dateOfArrival=" + dateOfArrival +
                ", dateOfReturn=" + dateOfReturn +
                ", completed=" + completed +
                '}';
    }
}
