package rent.vehicle.workerservicemodel.dto.worker;

import lombok.Getter;
import lombok.Setter;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;

import java.util.Set;
@Getter
@Setter
public class ResponseWorkerDto {
    private Long id;
    private String login;
    private String name;
    private Set<ResponseTicketDto> assignedTickets;
}
