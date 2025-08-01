package rent.vehicle.workerservicemodel.dto.ticket;

import lombok.Getter;
import lombok.Setter;
import rent.vehicle.workerservicemodel.dto.worker.ResponseWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.WorkerSummaryDto;
import rent.vehicle.workerservicemodel.enums.TicketStatus;
@Setter
@Getter
public class ResponseTicketDto {
    private Long id;
    private String createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private TicketStatus ticketStatus;
    private WorkerSummaryDto assignedTo;
}
