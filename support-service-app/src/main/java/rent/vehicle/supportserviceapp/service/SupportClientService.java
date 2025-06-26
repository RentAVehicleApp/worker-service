package rent.vehicle.supportserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.*;
import rent.vehicle.dto.request.CreateUserDto;
import rent.vehicle.support.model.dto.*;

public interface SupportClientService {


    Mono<UserDto> createUser(CreateUserDto userCreateDto);


    ResponseSupporterDto createSupporter(CreateSupporterDto createSupporterDto);

    ResponseSupporterDto updateSupporter(Long id, UpdateSupporterDto updateSupporterDto);

    ResponseSupporterDto findSupporter(Long id);

    Boolean removeSupporter(Long id);

    CreateTicketDto createTicker(CreateTicketDto createTicketDto);

    UpdateTicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto);

    ResponseTicketDto findTicket(Long id);

    Boolean removeTicket(Long id);
}
