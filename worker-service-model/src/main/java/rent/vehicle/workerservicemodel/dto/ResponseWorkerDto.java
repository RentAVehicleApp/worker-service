package rent.vehicle.workerservicemodel.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class ResponseWorkerDto {
    private Long id;
    private String login;
    private String name;
    private Set<ResponseTicketDto> assignedTickets;
}
