package rent.vehicle.workerservicemodel.dto.ticket;

import lombok.Getter;



import java.time.Instant;

@Getter
public class CreateTicketDto {
    private Long id;
    private Long createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private final Instant createdDate =  Instant.now();
    private final Instant updatedAt =  Instant.now();
}
