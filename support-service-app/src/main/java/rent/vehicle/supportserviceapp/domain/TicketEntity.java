package rent.vehicle.supportserviceapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rent.vehicle.support.model.dto.UserDto;
import rent.vehicle.support.model.enums.TicketStatus;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private TicketStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private SupporterEntity assignedTo;

    public void AssignTo(SupporterEntity assignedTo){
        this.assignedTo = assignedTo;
    }
    public void CloseTicket(){
        setStatus(TicketStatus.COMPLETED);
    }
    public boolean CanBeAssigned(){
        return getStatus() != TicketStatus.COMPLETED && getStatus() != TicketStatus.CANCELLED;
    }

}
