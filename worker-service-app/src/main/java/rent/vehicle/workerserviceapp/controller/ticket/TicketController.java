package rent.vehicle.workerserviceapp.controller.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rent.vehicle.workerserviceapp.service.ticket.TicketClientService;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.constants.ApiPaths;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.ticket.CreateTicketDto;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.ticket.UpdateTicketDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PATH_BASE)
public class TicketController {
    private final TicketClientService ticketClientService;
    @PostMapping(ApiPaths.PATH_TICKET)
    public ResponseTicketDto createTicket(@RequestBody CreateTicketDto createTicketDto) {
        return ticketClientService.createTicket(createTicketDto);
    }
    @PostMapping(ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID)
    public ResponseTicketDto updateTicket(@PathVariable Long ticketId, @RequestBody UpdateTicketDto updateTicketDto) {
        return ticketClientService.updateTicket(ticketId,updateTicketDto);
    }
    @GetMapping(ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID)
    public ResponseTicketDto getTicket(@PathVariable Long ticketId){

        return ticketClientService.findTicket(ticketId);
    }
    @DeleteMapping(ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID)
    public Boolean deleteTicket(@PathVariable Long ticketId){

        return ticketClientService.removeTicket(ticketId);
    }
    @PostMapping(ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID+ApiPaths.PATH_CLOSE)
    public ResponseTicketDto closeTicket(@PathVariable Long ticketId){

        return ticketClientService.closeTicket(ticketId);
    }

    @GetMapping(ApiPaths.PATH_TICKETS)
    public CustomPage<ResponseTicketDto> getAllTickets(){

        return ticketClientService.getAllTickets();
    }
    @GetMapping(ApiPaths.PATH_SEARCH+ApiPaths.PATH_TICKETS)
    public CustomPage<ResponseTicketDto> searchTickets(@ModelAttribute GenericSearchRequest genericSearchRequest) {
        return ticketClientService.searchTickets(genericSearchRequest);
    }


}