package rent.vehicle.workerserviceapp.service.worker;

import org.springframework.data.domain.Pageable;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.specification.SearchCriteria;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.*;

import java.util.Map;


public interface WorkerClientService {




    ResponseWorkerDto createWorker(CreateWorkerDto createWorkerDto);

    ResponseWorkerDto updateWorker(Long id, UpdateWorkerDto updateWorkerDto);

    ResponseWorkerDto findWorker(Long id);

    Boolean removeWorker(Long id);

    int getWorkerWorkload(Long id);

    ResponseTicketDto assignTicket(Long ticketId, Long workerId);

    ResponseTicketDto reassignTicket(Long ticketId, Long workerId);

    CustomPage<WorkerWithTicketsDto> getAllWorkers(Pageable pageable);

    CustomPage<ResponseWorkerDto> searchWorkers(String filter, Pageable pageable);

    void autoAssignTicket(Long ticketId);

    WorkerAuthDto findByLogin(String login);
}
