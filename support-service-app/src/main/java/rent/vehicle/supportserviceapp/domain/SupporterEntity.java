package rent.vehicle.supportserviceapp.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@EqualsAndHashCode(of="id")
public class SupporterEntity {
    @Id
    @Column(name = "supporter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //password
    private static final String password = "123456";
    @Column(name = "supporter_login")
    private String login;
    @Column(name = "supporter_name")
    private String name;
    @Column(name = "assigned_tickets_to_support")
    @OneToMany(mappedBy = "supporter")
    private Set<TicketEntity> assignedTickets;
    private static final int assignedTicketCapacity = 4;

    public void assignNewTicket(TicketEntity ticketEntity) {
        if(assignedTickets.size() < assignedTicketCapacity) {
            assignedTickets.add(ticketEntity);
        }
    }
}
