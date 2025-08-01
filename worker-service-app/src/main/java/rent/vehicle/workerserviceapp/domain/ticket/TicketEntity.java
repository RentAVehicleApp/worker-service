package rent.vehicle.workerserviceapp.domain.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import rent.vehicle.workerserviceapp.domain.worker.WorkerEntity;
import rent.vehicle.workerservicemodel.enums.TicketStatus;


import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    private Instant createdDate;
    private Instant updatedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    @JsonIgnoreProperties({"assignedTickets"})
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

    @PrePersist
    public void prePersist(){
        if (ticketStatus == null) {
            ticketStatus = TicketStatus.TODO;
        }
        if (createdDate == null) {
            createdDate = Instant.now();
        }
        if (updatedDate == null) {
            updatedDate = Instant.now();
        }
    }

}
