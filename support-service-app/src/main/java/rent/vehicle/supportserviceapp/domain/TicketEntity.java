package rent.vehicle.supportserviceapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rent.vehicle.support.model.dto.UserDto;
import rent.vehicle.support.model.enums.TicketStatus;

import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private TicketStatus status;
    private Instant createdDate;
    private Instant updatedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SupporterEntity assignedTo;

    public void assignTo(SupporterEntity assignedTo){
        this.assignedTo = assignedTo;
    }
    public void closeTicket(){
        setStatus(TicketStatus.COMPLETED);
    }
    public boolean canBeAssigned(){
        return getStatus() != TicketStatus.COMPLETED && getStatus() != TicketStatus.CANCELLED;
    }

}
