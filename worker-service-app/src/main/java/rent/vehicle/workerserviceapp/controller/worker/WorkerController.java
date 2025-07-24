package rent.vehicle.workerserviceapp.controller.worker;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import rent.vehicle.workerserviceapp.service.worker.WorkerClientService;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.constants.ApiPaths;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.CreateWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.ResponseWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.UpdateWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.WorkerWithTicketsDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PATH_BASE)
public class WorkerController {
    private final WorkerClientService workerClientService;

    @PostMapping
    public ResponseWorkerDto createWorker(@RequestBody CreateWorkerDto createWorkerDto) {
        return workerClientService.createWorker(createWorkerDto);
    }
    @PostMapping(ApiPaths.PATH_WORKER_ID)
    public ResponseWorkerDto updateSupporter(@PathVariable Long workerId, @RequestBody UpdateWorkerDto updateWorkerDto) {
        return workerClientService.updateWorker(workerId,updateWorkerDto);
    }
    @GetMapping(ApiPaths.PATH_WORKER_ID)
    public ResponseWorkerDto getSupporter(@PathVariable Long workerId) {

        return workerClientService.findWorker(workerId);
    }
    @DeleteMapping(ApiPaths.PATH_WORKER_ID)
    public Boolean deleteSupporter(@PathVariable Long workerId) {

        return workerClientService.removeWorker(workerId);
    }

    @GetMapping(ApiPaths.PATH_WORKLOAD+ApiPaths.PATH_WORKER_ID)
    public int GetSupporterWorkload(@PathVariable Long workerId){

        return workerClientService.getWorkerWorkload(workerId);
    }

    @PostMapping(ApiPaths.PATH_WORKER_ID+ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID+ApiPaths.PATH_ASSIGN)
    public ResponseTicketDto assignTicket(@PathVariable Long ticketId, @PathVariable Long workerId){
        return workerClientService.assignTicket(ticketId,workerId);
    }
    @PostMapping(ApiPaths.PATH_WORKER_ID+ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID+ApiPaths.PATH_REASSIGN)
    public ResponseTicketDto reassignTicket(@PathVariable Long ticketId, @PathVariable Long workerId){
        return workerClientService.reassignTicket(ticketId,workerId);
    }

    @GetMapping(ApiPaths.PATH_WORKERS)
    public CustomPage<WorkerWithTicketsDto> getAllSupporters( @PageableDefault(page = 0, size = 20, sort = "id,asc") Pageable pageable) {
        return workerClientService.getAllWorkers(pageable);
    }
    @GetMapping(ApiPaths.PATH_SEARCH+ApiPaths.PATH_WORKERS)
    public CustomPage<ResponseWorkerDto> searchWorkers(@ModelAttribute GenericSearchRequest genericSearchRequest) {
        return workerClientService.searchWorkers(genericSearchRequest);
    }




}
