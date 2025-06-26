package rent.vehicle.support.model.dto;

import lombok.Getter;
import lombok.Setter;
import rent.vehicle.support.model.enums.TicketStatus;

import java.time.Instant;

@Setter
@Getter
public class UpdateTicketDto {
    private String header;
    private String problem;
    private TicketStatus status;
    private Instant createdAt;
    private CreateSupporterDto assignedTo;
}
