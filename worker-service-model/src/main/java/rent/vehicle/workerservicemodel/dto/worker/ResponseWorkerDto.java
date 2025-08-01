package rent.vehicle.workerservicemodel.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWorkerDto {
    private Long id;
    private String login;
    private String name;
    private Set<ResponseTicketDto> assignedTickets;
}
