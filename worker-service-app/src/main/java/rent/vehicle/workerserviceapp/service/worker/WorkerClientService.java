package rent.vehicle.workerserviceapp.service.worker;

import org.springframework.data.domain.Pageable;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.CreateWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.ResponseWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.UpdateWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.WorkerWithTicketsDto;


public interface WorkerClientService {




    ResponseWorkerDto createWorker(CreateWorkerDto createWorkerDto);

    ResponseWorkerDto updateWorker(Long id, UpdateWorkerDto updateWorkerDto);

    ResponseWorkerDto findWorker(Long id);

    Boolean removeWorker(Long id);

    int getWorkerWorkload(Long id);

    ResponseTicketDto assignTicket(Long ticketId, Long supporterId);

    ResponseTicketDto reassignTicket(Long ticketId, Long supporterId);

    CustomPage<WorkerWithTicketsDto> getAllWorkers(Pageable pageable);

    CustomPage<ResponseWorkerDto> searchWorkers(GenericSearchRequest request);

}
