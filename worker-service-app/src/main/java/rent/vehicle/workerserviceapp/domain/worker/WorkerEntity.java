package rent.vehicle.workerserviceapp.domain.worker;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rent.vehicle.workerserviceapp.domain.ticket.TicketEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@EqualsAndHashCode(of="id")
public class WorkerEntity {
    @Id
    @Column(name = "worker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //password
    private static final String password = "123456";
    @Column(name = "worker_login")
    private String login;
    @Column(name = "worker_name")
    private String name;
    @Column(name = "assigned_tickets_to_worker")
    @OneToMany(mappedBy = "assignedTo")
    private Set<TicketEntity> assignedTickets = new HashSet<TicketEntity>();
    private static final int assignedTicketCapacity = 4;
    private Set<Role> roles;

    public void assignNewTicket(TicketEntity ticketEntity) {
        if(assignedTickets.size() < assignedTicketCapacity) {
            assignedTickets.add(ticketEntity);
        }
    }
}
