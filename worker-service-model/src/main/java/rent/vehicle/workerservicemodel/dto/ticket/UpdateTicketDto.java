package rent.vehicle.workerservicemodel.dto.ticket;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class UpdateTicketDto {
    private String header;
    private String problem;
    private final Instant updatedTime = Instant.now();
}
