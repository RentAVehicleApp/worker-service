package rent.vehicle.workerserviceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import rent.vehicle.workerserviceapp.service.WorkerClientService;
import rent.vehicle.workerservicemodel.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/worker")
public class WorkerController {
    private final WorkerClientService workerClientService;



    @PostMapping("/supporter")
    public ResponseWorkerDto createWorker(@RequestBody CreateWorkerDto createWorkerDto) {
        return workerClientService.createWorker(createWorkerDto);
    }
    @PostMapping("supporter/{id}/update")
    public ResponseWorkerDto updateSupporter(@PathVariable Long id, @RequestBody UpdateWorkerDto updateWorkerDto) {
        return workerClientService.updateWorker(id,updateWorkerDto);
    }
    @GetMapping("supporter/{id}")
    public ResponseWorkerDto getSupporter(@PathVariable Long id) {

        return workerClientService.findWorker(id);
    }
    @DeleteMapping("supporter/{id}")
    public Boolean deleteSupporter(@PathVariable Long id) {

        return workerClientService.removeWorker(id);
    }
    @PostMapping("/ticket")
    public ResponseTicketDto createTicket(@RequestBody CreateTicketDto createTicketDto) {
        return workerClientService.createTicket(createTicketDto);
    }
    @PostMapping("/ticket/{id}")
    public ResponseTicketDto updateTicket(@PathVariable Long id, @RequestBody UpdateTicketDto updateTicketDto) {
        return workerClientService.updateTicket(id,updateTicketDto);
    }
    @GetMapping("/ticket/{id}")
    public ResponseTicketDto getTicket(@PathVariable Long id){

        return workerClientService.findTicket(id);
    }
    @DeleteMapping("/ticket/{id}")
    public Boolean deleteTicket(@PathVariable Long id){

        return workerClientService.removeTicket(id);
    }
    @PostMapping("/ticket/{ticketId}/assign/{supporterId}")
    public ResponseTicketDto assignTicket(@PathVariable Long ticketId, @PathVariable Long supporterId){
        return workerClientService.assignTicket(ticketId,supporterId);
    }
    @PostMapping("/ticket/{id}/close")
    public ResponseTicketDto closeTicket(@PathVariable Long id){

        return workerClientService.closeTicket(id);
    }
    @PostMapping("ticket/{ticketId}/reassign/{supporterId}")
    public ResponseTicketDto reassignTicket(@PathVariable Long ticketId, @PathVariable Long supporterId){
        return workerClientService.reassignTicket(ticketId,supporterId);
    }
    @GetMapping("supporter/workload/{id}")
    public int GetSupporterWorkload(@PathVariable Long id){

        return workerClientService.getWorkerWorkload(id);
    }
    @GetMapping("/tickets")
    public Page<ResponseTicketDto> getAllTickets(){

        return workerClientService.getAllTickets();
    }
    @GetMapping("/supporters")
    public Page<WorkerWithTicketsDto> getAllSupporters(@RequestBody Pageable pageable){
        return workerClientService.getAllWorkers(pageable);
    }



}
