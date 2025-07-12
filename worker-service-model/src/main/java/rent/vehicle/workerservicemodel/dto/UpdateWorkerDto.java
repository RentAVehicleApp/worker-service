package rent.vehicle.workerservicemodel.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Setter
@Getter
public class UpdateWorkerDto {
    private String login;
    private String name;
    private Set<CreateTicketDto> assignedTickets;
}
