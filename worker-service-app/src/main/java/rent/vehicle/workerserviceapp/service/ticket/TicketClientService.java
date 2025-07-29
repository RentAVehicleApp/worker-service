package rent.vehicle.workerserviceapp.service.ticket;

import org.springframework.data.domain.Pageable;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.ticket.CreateTicketDto;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.ticket.UpdateTicketDto;

public interface TicketClientService {
    ResponseTicketDto createTicket(CreateTicketDto createTicketDto);

    ResponseTicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto);

    ResponseTicketDto findTicket(Long id);

    Boolean removeTicket(Long id);

    ResponseTicketDto closeTicket(Long id);

    void autoAssignTicket(Long ticketId);

    CustomPage<ResponseTicketDto> getAllTickets();

    CustomPage<ResponseTicketDto> searchTickets(String filter, Pageable pageable);
}
