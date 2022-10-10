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
    @JoinColumn(name = "client_id")
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
    @ManyToMany
    @JoinTable(
            name = "booking_rider",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "rider_id")
    )
    private List<Rider> listOfRiders;

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

    public List<Rider> getListOfRiders() {
        if (listOfRiders == null) {
            return new ArrayList<>();
        }
        return listOfRiders;
    }

    public void setListOfRiders(List<Rider> listOfRiders) {
        this.listOfRiders = listOfRiders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return completed == booking.completed && Objects.equals(id, booking.id) &&
                Objects.equals(client, booking.client) && Objects.equals(dateOfArrival, booking.dateOfArrival) &&
                Objects.equals(dateOfReturn, booking.dateOfReturn) && Objects.equals(listOfRiders, booking.listOfRiders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, dateOfArrival, dateOfReturn, completed, listOfRiders);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", client=" + client.toString() +
                ", dateOfArrival=" + dateOfArrival +
                ", dateOfReturn=" + dateOfReturn +
                ", completed=" + completed +
                ", listOfRiders=" + Arrays.toString(listOfRiders.toArray()) +
                '}';
    }
}
