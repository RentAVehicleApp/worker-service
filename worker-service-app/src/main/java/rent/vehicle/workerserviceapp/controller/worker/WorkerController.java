package rent.vehicle.workerserviceapp.controller.worker;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import rent.vehicle.workerserviceapp.service.worker.WorkerClientService;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.constants.ApiPaths;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.specification.SearchCriteria;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PATH_BASE)
public class WorkerController {
    private final WorkerClientService workerClientService;

    @GetMapping(ApiPaths.PATH_HEALTH)
    public String health() {
        return "OK";
    }
    @PostMapping
    public ResponseWorkerDto createWorker(@RequestBody CreateWorkerDto createWorkerDto) {
        return workerClientService.createWorker(createWorkerDto);
    }
    @PostMapping(ApiPaths.PATH_WORKER_ID)
    public ResponseWorkerDto updateWorker(@PathVariable Long workerId, @RequestBody UpdateWorkerDto updateWorkerDto) {
        return workerClientService.updateWorker(workerId,updateWorkerDto);
    }
    @GetMapping(ApiPaths.PATH_WORKER_ID)
    public ResponseWorkerDto getWorker(@PathVariable Long workerId) {

        return workerClientService.findWorker(workerId);
    }
    @DeleteMapping(ApiPaths.PATH_WORKER_ID)
    public Boolean deleteWorker(@PathVariable Long workerId) {

        return workerClientService.removeWorker(workerId);
    }

    @GetMapping(ApiPaths.PATH_WORKLOAD+ApiPaths.PATH_WORKER_ID)
    public int getWorkerWorkload(@PathVariable Long workerId){

        return workerClientService.getWorkerWorkload(workerId);
    }

    @PostMapping(ApiPaths.PATH_WORKER_ID+ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID+ApiPaths.PATH_ASSIGN)
    public ResponseTicketDto assignTicket(@PathVariable Long ticketId, @PathVariable Long workerId){
        return workerClientService.assignTicket(ticketId,workerId);
    }
    @PostMapping(ApiPaths.PATH_WORKER_ID+ApiPaths.PATH_TICKET+ApiPaths.PATH_TICKET_ID+ApiPaths.PATH_REASSIGN)//TODO FIX now working currently erasing ticket from one worker but the other dont get it
    public ResponseTicketDto reassignTicket(@PathVariable Long ticketId, @PathVariable Long workerId){
        return workerClientService.reassignTicket(ticketId,workerId);
    }

    @GetMapping(ApiPaths.PATH_WORKERS)
    public CustomPage<WorkerWithTicketsDto> getAllWorkers( @PageableDefault(page = 0, size = 20, direction = Sort.Direction.ASC) Pageable pageable) {
        return workerClientService.getAllWorkers(pageable);
    }
    @GetMapping(ApiPaths.PATH_SEARCH+ApiPaths.PATH_WORKERS)
    public CustomPage<ResponseWorkerDto> searchWorkers( @RequestParam(required = false) String filter,Pageable pageable) {
        return workerClientService.searchWorkers(filter,pageable);
    }
    @GetMapping(ApiPaths.PATH_LOGIN)
    public WorkerAuthDto findByLogin(@RequestParam String login) {
        return workerClientService.findByLogin(login);
    }




}
