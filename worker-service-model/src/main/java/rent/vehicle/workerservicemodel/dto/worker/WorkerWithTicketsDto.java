package rent.vehicle.workerservicemodel.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerWithTicketsDto {
        String login;
        Set<ResponseTicketDto> assignedTickets;
}
