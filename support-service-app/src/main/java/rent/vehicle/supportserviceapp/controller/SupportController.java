package rent.vehicle.supportserviceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.request.CreateUserDto;
import rent.vehicle.support.model.dto.*;
import rent.vehicle.supportserviceapp.service.SupportClientService;

import java.util.Map;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/support")
public class SupportController {
    private final SupportClientService supportClientService;

    @PostMapping("/user")
    public Mono<UserDto> createUserDto(@RequestBody CreateUserDto userCreateDto) {
        return supportClientService.createUser(userCreateDto);
    }

    @PostMapping("/supporter")
    public ResponseSupporterDto createSupporter(@RequestBody CreateSupporterDto createSupporterDto) {
        return supportClientService.createSupporter(createSupporterDto);
    }
    @PostMapping("supporter/{id}/update")
    public ResponseSupporterDto updateSupporter(@PathVariable Long id, @RequestBody UpdateSupporterDto updateSupporterDto) {
        return supportClientService.updateSupporter(id,updateSupporterDto);
    }
    @GetMapping("supporter/{id}")
    public ResponseSupporterDto getSupporter(@PathVariable Long id) {

        return supportClientService.findSupporter(id);
    }
    @DeleteMapping("supporter/{id}")
    public Boolean deleteSupporter(@PathVariable Long id) {

        return supportClientService.removeSupporter(id);
    }
    @PostMapping("/ticket")
    public ResponseTicketDto createTicket(@RequestBody CreateTicketDto createTicketDto) {
        return supportClientService.createTicket(createTicketDto);
    }
    @PostMapping("/ticket/{id}")
    public ResponseTicketDto updateTicket(@PathVariable Long id, @RequestBody UpdateTicketDto updateTicketDto) {
        return supportClientService.updateTicket(id,updateTicketDto);
    }
    @GetMapping("/ticket/{id}")
    public ResponseTicketDto getTicket(@PathVariable Long id){

        return supportClientService.findTicket(id);
    }
    @DeleteMapping("/ticket/{id}")
    public Boolean deleteTicket(@PathVariable Long id){

        return supportClientService.removeTicket(id);
    }
    @PostMapping("/ticket/{ticketId}/assign/{supporterId}")
    public ResponseTicketDto assignTicket(@PathVariable Long ticketId, @PathVariable Long supporterId){
        return supportClientService.assignTicket(ticketId,supporterId);
    }
    @PostMapping("/ticket/{id}/close")
    public ResponseTicketDto closeTicket(@PathVariable Long id){

        return supportClientService.closeTicket(id);
    }
    @PostMapping("ticket/{ticketId}/reassign/{supporterId}")
    public ResponseTicketDto reassignTicket(@PathVariable Long ticketId, @PathVariable Long supporterId){
        return supportClientService.reassignTicket(ticketId,supporterId);
    }
    @GetMapping("supporter/{id}")
    public int GetSupporterWorkload(@PathVariable Long id){

        return supportClientService.getSupporterWorkload(id);
    }
    @GetMapping("/tickets")
    public Page<ResponseTicketDto> getAllTickets(){

        return supportClientService.getAllTickets();
    }
    @GetMapping("/supporters")
    public Page<SupportWithTicketsDto> getAllSupporters(@RequestBody Pageable pageable){
        return supportClientService.getAllSupporters(pageable);
    }



}
