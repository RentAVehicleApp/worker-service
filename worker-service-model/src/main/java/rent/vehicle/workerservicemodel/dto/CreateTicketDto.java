package rent.vehicle.workerservicemodel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rent.vehicle.workerservicemodel.enums.TicketStatus;


import java.time.Instant;

@AllArgsConstructor
@Getter
public class CreateTicketDto {
    private Long id;
    private Long createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private final Instant createdDate =  Instant.now();
    private final Instant updatedAt =  Instant.now();
    private final TicketStatus status = TicketStatus.TODO;
}
