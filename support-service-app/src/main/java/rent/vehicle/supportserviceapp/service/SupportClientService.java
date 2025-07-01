package rent.vehicle.supportserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.*;
import rent.vehicle.dto.request.CreateUserDto;
import rent.vehicle.support.model.dto.*;

import java.util.Map;
import java.util.Set;

public interface SupportClientService {


    Mono<UserDto> createUser(CreateUserDto userCreateDto);


    ResponseSupporterDto createSupporter(CreateSupporterDto createSupporterDto);

    ResponseSupporterDto updateSupporter(Long id, UpdateSupporterDto updateSupporterDto);

    ResponseSupporterDto findSupporter(Long id);

    Boolean removeSupporter(Long id);

    ResponseTicketDto createTicket(CreateTicketDto createTicketDto);

    ResponseTicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto);

    ResponseTicketDto findTicket(Long id);

    Boolean removeTicket(Long id);

    ResponseTicketDto assignTicket(Long ticketId, Long supporterId);

    ResponseTicketDto closeTicket(Long id);

    ResponseTicketDto reassignTicket(Long ticketId, Long supporterId);

    int getSupporterWorkload(Long id);

    Page<ResponseTicketDto> getAllTickets();

    Page<SupportWithTicketsDto> getAllSupporters(Pageable pageable);

    void autoAssignTicket(Long ticketId);
}
