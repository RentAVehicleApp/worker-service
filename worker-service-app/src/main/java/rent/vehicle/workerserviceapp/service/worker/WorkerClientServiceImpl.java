package rent.vehicle.workerserviceapp.service.worker;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import rent.vehicle.workerserviceapp.common.SearchCriteriaParser;
import rent.vehicle.workerserviceapp.domain.ticket.TicketEntity;
import rent.vehicle.workerserviceapp.domain.worker.WorkerEntity;
import rent.vehicle.workerserviceapp.event.TicketCreatedEventPublisher;
import rent.vehicle.workerserviceapp.repository.ticket.TicketRepository;
import rent.vehicle.workerserviceapp.repository.worker.WorkerRepository;
import rent.vehicle.workerserviceapp.service.specification.GenericSpecificationBuilder;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.specification.SearchCriteria;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.CreateWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.ResponseWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.UpdateWorkerDto;
import rent.vehicle.workerservicemodel.dto.worker.WorkerWithTicketsDto;
import rent.vehicle.workerservicemodel.enums.Operations;
import rent.vehicle.workerservicemodel.exception.TicketCannotBeAssignedException;
import rent.vehicle.workerservicemodel.exception.WorkerNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerClientServiceImpl implements WorkerClientService {


    private final ModelMapper modelMapper;
    private final WorkerRepository workerRepository;
    private final TicketRepository ticketRepository;
    private final TicketCreatedEventPublisher publisher;
    //ToDO change specification name
    private final GenericSpecificationBuilder<WorkerEntity> genericSpecificationBuilder;
    private final SearchCriteriaParser searchCriteriaParser;
    private final GenericSpecificationBuilder<TicketEntity> ticketSpecificationBuilder;


    @Override
    @Transactional
    public ResponseWorkerDto createWorker(CreateWorkerDto createWorkerDto) {
        WorkerEntity workerEntity = new WorkerEntity();
        workerEntity.setName(createWorkerDto.getName());
        workerEntity.setLogin(createWorkerDto.getLogin());
        workerRepository.save(workerEntity);
        ResponseWorkerDto response = new ResponseWorkerDto();
        response.setId(workerEntity.getId());
        response.setLogin(workerEntity.getLogin());
        response.setName(workerEntity.getName());

        // Мапим коллекцию тикетов вручную, используя ModelMapper для каждого тикета
        Set<ResponseTicketDto> ticketDtos = workerEntity.getAssignedTickets().stream()
                .map(ticket -> modelMapper.map(ticket, ResponseTicketDto.class))
                .collect(Collectors.toSet());

        response.setAssignedTickets(ticketDtos);
        return response;
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseWorkerDto updateWorker(Long id, UpdateWorkerDto updateWorkerDto) {
        Optional<WorkerEntity> workerEntity = workerRepository.findById(id);
        if (workerEntity.isPresent()) {
            if (updateWorkerDto.getLogin() != null && !updateWorkerDto.getLogin().isEmpty()) {
                workerEntity.get().setLogin(updateWorkerDto.getLogin());
            }
            if (updateWorkerDto.getName() != null && !updateWorkerDto.getName().isEmpty()) {
                workerEntity.get().setName(updateWorkerDto.getName());
            }
            workerRepository.save(workerEntity.get());

            // Мапим основные поля через ModelMapper
            ResponseWorkerDto response = new ResponseWorkerDto();
            response.setId(workerEntity.get().getId());
            response.setLogin(workerEntity.get().getLogin());
            response.setName(workerEntity.get().getName());

            // Мапим коллекцию тикетов вручную, используя ModelMapper для каждого тикета
            Set<ResponseTicketDto> ticketDtos = workerEntity.get().getAssignedTickets().stream()
                    .map(ticket -> modelMapper.map(ticket, ResponseTicketDto.class))
                    .collect(Collectors.toSet());

            response.setAssignedTickets(ticketDtos);
            return response;
        }
        return null;
    }


    @Override
    public ResponseWorkerDto findWorker(Long id) {
        WorkerEntity workerEntity = workerRepository.findById(id).isPresent()?workerRepository.findById(id).get():null;
        if(workerEntity==null){
            throw new WorkerNotFoundException(id);
        }
        // Мапим основные поля через ModelMapper
        ResponseWorkerDto response = new ResponseWorkerDto();
        response.setId(workerEntity.getId());
        response.setLogin(workerEntity.getLogin());
        response.setName(workerEntity.getName());

        // Мапим коллекцию тикетов вручную, используя ModelMapper для каждого тикета
        Set<ResponseTicketDto> ticketDtos = workerEntity.getAssignedTickets().stream()
                .map(ticket -> modelMapper.map(ticket, ResponseTicketDto.class))
                .collect(Collectors.toSet());

        response.setAssignedTickets(ticketDtos);
        return response;
    }
    @Transactional
    @Override
    public Boolean removeWorker(Long id) {
        if(workerRepository.findById(id).isPresent()){
            workerRepository.deleteById(id);
            return true;
        }
        return false;
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
    @Transactional
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
    @Transactional
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
    public CustomPage<ResponseWorkerDto> searchWorkers(String filter, Pageable pageable) {
        GenericSearchRequest request = searchCriteriaParser.buildSearchRequest(filter, pageable);
        String[] parts = request.getSort().split(",");
        String sortField = parts[0];
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1].trim())
                : Sort.Direction.ASC; // по умолчанию ASC, если не указано

        Sort sort = Sort.by(new Sort.Order(direction, sortField));

        pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<WorkerEntity> spec = genericSpecificationBuilder.buildFromRequest(request);
        Page<WorkerEntity> page = workerRepository.findAll(spec, pageable);
        CustomPage<WorkerEntity> customPage = CustomPage.from(page);
        return customPage.map(entity -> modelMapper.map(entity, ResponseWorkerDto.class));
    }

    @Override
    public void autoAssignTicket(Long ticketId) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<WorkerEntity> page = workerRepository.findAllByOrderByAssignedTicketsAsc(pageable);
        if (page.hasContent()) {
            WorkerEntity assignTo = page.getContent().getFirst();
           assignTicket(ticketId, assignTo.getId());
        } else {
            throw new TicketCannotBeAssignedException(ticketId);
        }
    }


}





//TODO AutoAssignTicket with TicketEvent



