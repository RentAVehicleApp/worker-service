package rent.vehicle.workerserviceapp.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import rent.vehicle.workerserviceapp.domain.TicketEntity;
import rent.vehicle.workerserviceapp.domain.WorkerEntity;
import rent.vehicle.workerserviceapp.event.TicketCreatedEventPublisher;
import rent.vehicle.workerserviceapp.repository.TicketRepository;
import rent.vehicle.workerserviceapp.repository.WorkerRepository;
import rent.vehicle.workerserviceapp.service.specification.WorkerSpecificationBuilder;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.*;
import rent.vehicle.workerservicemodel.exception.TicketCannotBeAssignedException;
import rent.vehicle.workerservicemodel.exception.WorkerNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerClientServiceImpl implements WorkerClientService {

    private final ModelMapper modelMapper;
    private final WorkerRepository workerRepository;
    private final TicketRepository ticketRepository;
    private final TicketCreatedEventPublisher publisher;
    //ToDO change specification name
    private final WorkerSpecificationBuilder<WorkerEntity> workerSpecificationBuilder;
    private final WorkerSpecificationBuilder<TicketEntity> ticketSpecificationBuilder;


    @Override
    public ResponseWorkerDto createWorker(CreateWorkerDto createWorkerDto) {
        WorkerEntity workerEntity = new WorkerEntity();
        workerEntity.setName(createWorkerDto.getName());
        workerEntity.setLogin(createWorkerDto.getLogin());
        workerRepository.save(workerEntity);
        return modelMapper.map(workerEntity, ResponseWorkerDto.class);
    }

    @Override
    public ResponseWorkerDto updateWorker(Long id, UpdateWorkerDto updateWorkerDto) {
       Optional<WorkerEntity> workerEntity = workerRepository.findById(id);
       if (workerEntity.isPresent()) {
           if (updateWorkerDto.getLogin() != null && !updateWorkerDto.getLogin().isEmpty()) {
               workerEntity.get().setLogin(updateWorkerDto.getLogin());
           }
           if (updateWorkerDto.getName() != null && !updateWorkerDto.getName().isEmpty()) {
               workerEntity.get().setName(updateWorkerDto.getName());
           }
           if (updateWorkerDto.getAssignedTickets() != null && !updateWorkerDto.getAssignedTickets().isEmpty()) {
               workerEntity.get().setAssignedTickets(updateWorkerDto.getAssignedTickets().stream()
                       .map(t->modelMapper.map(t, TicketEntity.class))
                       .collect(Collectors.toSet()));
           }
           workerRepository.save(workerEntity.get());
           return modelMapper.map(workerEntity.get(),ResponseWorkerDto.class);
       }
     return null;
    }

    @Override
    public ResponseWorkerDto findWorker(Long id) {
        WorkerEntity supporterEntity = workerRepository.findById(id).isPresent()?workerRepository.findById(id).get():null;
        if(supporterEntity==null){
            throw new WorkerNotFoundException(id);
        }
        return modelMapper.map(supporterEntity,ResponseWorkerDto.class);
    }

    @Override
    public Boolean removeWorker(Long id) {
       if(workerRepository.findById(id).isPresent()){
           workerRepository.deleteById(id);
           return true;
       }
        return false;
    }

    @Override
    public ResponseTicketDto createTicket(CreateTicketDto createTicketDto) {
        //TODO Add Security Validaiton for only users with USER role can access this method
        TicketEntity ticketEntity = modelMapper.map(createTicketDto, TicketEntity.class);
        ticketRepository.save(ticketEntity);
        publisher.publishTicketCreatedEvent(ticketEntity);
        return modelMapper.map(ticketEntity, ResponseTicketDto.class);
    }

    @Override
    public ResponseTicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto) {
        TicketEntity ticketEntity = ticketRepository.findById(id).isPresent()?ticketRepository.findById(id).get():null;
        if(ticketEntity==null){
            return  null;
        }else{
            if(!updateTicketDto.getHeader().isEmpty()){
                ticketEntity.setHeader(updateTicketDto.getHeader());
            }
            if(!updateTicketDto.getProblem().isEmpty()){
                ticketEntity.setProblem(updateTicketDto.getProblem());
            }
            ticketEntity.setUpdatedDate(updateTicketDto.getUpdatedTime());
            ticketRepository.save(ticketEntity);
            return modelMapper.map(ticketEntity, ResponseTicketDto.class);
        }
    }

    @Override
    public ResponseTicketDto findTicket(Long id) {
        TicketEntity ticketEntity = ticketRepository.findById(id).get();
        return modelMapper.map(ticketEntity,ResponseTicketDto.class);
    }

    @Override
    public Boolean removeTicket(Long id) {
        //TODO Add Security Validation for only supporters can remove Tickets
        TicketEntity ticketEntity = ticketRepository.findById(id).get();
        if(ticketEntity==null){
            return false;
        }
        ticketRepository.delete(ticketEntity);
        return true;
    }

    @Override
    public ResponseTicketDto assignTicket(Long ticketId, Long supporterId) {
        //TODO Add Security Validation for only supporters can assign Tickets
        TicketEntity ticketEntity = ticketRepository.findById(ticketId).get();
        WorkerEntity workerEntity = workerRepository.findById(supporterId).get();
        if(workerEntity==null||ticketEntity==null){
            throw new RuntimeException();
        }
        if(ticketEntity.canBeAssigned()){
            workerEntity.assignNewTicket(ticketEntity);
        }else{
            throw new TicketCannotBeAssignedException(ticketId);
        }
        ticketEntity.assignTo(workerEntity);
        ticketRepository.save(ticketEntity);
        workerRepository.save(workerEntity);
        return modelMapper.map(TicketEntity.class,ResponseTicketDto.class);
    }

    @Override
    public ResponseTicketDto closeTicket(Long id) {
        //TODO Add Security Validation for only supporters can close Tickets
        TicketEntity ticketEntity = ticketRepository.findById(id).get();
        ticketEntity.closeTicket();
        return modelMapper.map(ticketEntity,ResponseTicketDto.class);
    }

    @Override
    public ResponseTicketDto reassignTicket(Long ticketId, Long toSupporterId) {
        //TODO Add Security Validation for only supporters can reassign Tickets
        if(workerRepository.findById(toSupporterId).isPresent()&&ticketRepository.findById(ticketId).isPresent()){
            TicketEntity ticket = ticketRepository.findById(ticketId).get();
            WorkerEntity assignToSupporter = workerRepository.findById(toSupporterId).get();
            WorkerEntity reassignFromSupporterEntity = workerRepository.findById(ticket.getAssignedTo().getId()).get();
            reassignFromSupporterEntity.getAssignedTickets().remove(ticket);
            if(ticket.canBeAssigned()){
                assignToSupporter.assignNewTicket(ticket);
                ticket.assignTo(assignToSupporter);
                ticketRepository.save(ticket);
                workerRepository.save(assignToSupporter);
                workerRepository.save(reassignFromSupporterEntity);
                return modelMapper.map(ticket, ResponseTicketDto.class);
            }
        }
        return null;
    }
//TODO decide if neeeded?
    @Override
    public int getWorkerWorkload(Long id) {
        if(!workerRepository.findById(id).isPresent()){
          throw new WorkerNotFoundException(id);
        }
        WorkerEntity supporterEntity = workerRepository.findById(id).get();
        return supporterEntity.getAssignedTickets().size();
    }

    @Override
    public CustomPage<ResponseTicketDto> getAllTickets() {
        //TODO Add Security Validation for only supporters can get all Tickets
        Pageable pageable = PageRequest.of(0, 10);
        Page<TicketEntity> allTickets = ticketRepository.findAll(pageable);
        CustomPage<TicketEntity> allCustomPageTickets = CustomPage.from(allTickets);
        return allCustomPageTickets.map(ticket->modelMapper.map(ticket,ResponseTicketDto.class));
    }

    @Override
    public CustomPage<WorkerWithTicketsDto> getAllWorkers(Pageable pageable) {
        Page<WorkerEntity> allWorkersWithTicketsPage = workerRepository.findAll(pageable);
        CustomPage<WorkerEntity> allWorkersWithTicketsCustomPage = CustomPage.from(allWorkersWithTicketsPage);

        return allWorkersWithTicketsCustomPage
                .map(supporterEntity ->{
                    WorkerWithTicketsDto workerWithTicketsDto = new WorkerWithTicketsDto();
                    workerWithTicketsDto.setLogin(supporterEntity.getLogin());
                    workerWithTicketsDto.setAssignedTickets(supporterEntity.getAssignedTickets().stream()
                            .map(assignedTicketEntity -> modelMapper.map(assignedTicketEntity, ResponseTicketDto.class))
                            .collect(Collectors.toSet()));
                    return workerWithTicketsDto;
                });
    }

    @Override
    public void autoAssignTicket(Long ticketId) {
    Page<WorkerEntity> allWorkers = workerRepository.findAllByOrderByAssignedTicketsAsc(PageRequest.of(0, 1));
    if(allWorkers.hasContent()){
        WorkerEntity workerEntity = allWorkers.getContent().getFirst();
        assignTicket(workerEntity.getId(), ticketId);
    }else{
        throw new TicketCannotBeAssignedException(ticketId);
    }

    }

    @Override
    public CustomPage<ResponseWorkerDto> searchWorkers(GenericSearchRequest request) {
        String[] parts = request.getSort().split(",");
        String sortField = parts[0];
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1].trim())
                : Sort.Direction.ASC; // по умолчанию ASC, если не указано

        Sort sort = Sort.by(new Sort.Order(direction, sortField));

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<WorkerEntity> spec = workerSpecificationBuilder.buildFromRequest(request);
        Page<WorkerEntity> page = workerRepository.findAll(spec, pageable);
        CustomPage<WorkerEntity> customPage = CustomPage.from(page);
        return customPage.map(entity -> modelMapper.map(entity, ResponseWorkerDto.class));
    }

    @Override
    public CustomPage<ResponseTicketDto> searchTickets(GenericSearchRequest request) {
        String[] parts = request.getSort().split(",");
        //TODO поставить sort внутри спецификациибилдер
        String sortField = parts[0];
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1].trim())
                : Sort.Direction.ASC; // по умолчанию ASC, если не указано

        Sort sort = Sort.by(new Sort.Order(direction, sortField));

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<TicketEntity> spec = ticketSpecificationBuilder.buildFromRequest(request);
        Page<TicketEntity> page = ticketRepository.findAll(spec, pageable);
        CustomPage<TicketEntity> customPage = CustomPage.from(page);
        return customPage.map(entity -> modelMapper.map(entity, ResponseTicketDto.class));
    }


    //TODO AutoAssignTicket with TicketEvent


}
