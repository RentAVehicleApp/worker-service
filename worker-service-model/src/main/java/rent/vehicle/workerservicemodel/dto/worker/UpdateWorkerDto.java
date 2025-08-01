package rent.vehicle.workerservicemodel.dto.worker;

import lombok.Getter;
import lombok.Setter;
import rent.vehicle.workerservicemodel.dto.ticket.CreateTicketDto;

import java.util.Set;
@Setter
@Getter
public class UpdateWorkerDto {
    private String login;
    private String name;
}
