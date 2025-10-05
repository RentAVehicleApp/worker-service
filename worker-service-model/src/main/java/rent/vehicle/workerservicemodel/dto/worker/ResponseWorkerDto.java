package rent.vehicle.workerservicemodel.dto.worker;

import lombok.*;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseWorkerDto {
    private Long id;
    private String login;
    private String name;
    private Set<ResponseTicketDto> assignedTickets;
}
