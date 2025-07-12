package rent.vehicle.workerserviceapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rent.vehicle.workerservicemodel.enums.TicketStatus;


import java.time.Instant;

@RequiredArgsConstructor
@Entity
@Getter
@Setter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private TicketStatus ticketStatus;
    private Instant createdDate;
    private Instant updatedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private WorkerEntity assignedTo;

    public void assignTo(WorkerEntity assignedTo){
        this.assignedTo = assignedTo;
    }
    public void closeTicket(){
        setTicketStatus(TicketStatus.COMPLETED);
    }
    public boolean canBeAssigned(){
        return getTicketStatus() != TicketStatus.COMPLETED && getTicketStatus() != TicketStatus.CANCELLED;
    }

}
