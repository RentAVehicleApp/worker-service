package rent.vehicle.workerserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rent.vehicle.workerservicemodel.dto.*;




public interface WorkerClientService {




    ResponseWorkerDto createWorker(CreateWorkerDto createWorkerDto);

    ResponseWorkerDto updateWorker(Long id, UpdateWorkerDto updateWorkerDto);

    ResponseWorkerDto findWorker(Long id);

    Boolean removeWorker(Long id);

    ResponseTicketDto createTicket(CreateTicketDto createTicketDto);

    ResponseTicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto);

    ResponseTicketDto findTicket(Long id);

    Boolean removeTicket(Long id);

    ResponseTicketDto assignTicket(Long ticketId, Long supporterId);

    ResponseTicketDto closeTicket(Long id);

    ResponseTicketDto reassignTicket(Long ticketId, Long supporterId);

    int getWorkerWorkload(Long id);

    Page<ResponseTicketDto> getAllTickets();

    Page<WorkerWithTicketsDto> getAllWorkers(Pageable pageable);

    void autoAssignTicket(Long ticketId);
}
