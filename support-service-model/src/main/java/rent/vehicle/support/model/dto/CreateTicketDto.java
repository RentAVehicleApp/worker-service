package rent.vehicle.support.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rent.vehicle.support.model.enums.TicketStatus;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateTicketDto {
    private Long id;
    private String createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private Instant createdAt;
    private TicketStatus status;
}
