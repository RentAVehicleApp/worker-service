package rent.vehicle.workerserviceapp.service.worker;

import jakarta.persistence.EntityNotFoundException;
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
import rent.vehicle.workerserviceapp.service.specification.WorkerSpecification;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.specification.SearchCriteria;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.worker.*;
import rent.vehicle.workerservicemodel.enums.Operations;
import rent.vehicle.workerservicemodel.enums.Role;
import rent.vehicle.workerservicemodel.enums.TicketStatus;
import rent.vehicle.workerservicemodel.exception.TicketCannotBeAssignedException;
import rent.vehicle.workerservicemodel.exception.WorkerNotFoundException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerClientServiceImpl implements WorkerClientService {


    private final ModelMapper modelMapper;
    private final WorkerRepository workerRepository;
    private final TicketRepository ticketRepository;
    //ToDO change specification name
    private final GenericSpecificationBuilder<WorkerEntity> genericSpecificationBuilder;
    private final SearchCriteriaParser searchCriteriaParser;


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
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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
        TicketEntity ticketEntity = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));
        WorkerEntity workerEntity = workerRepository.findById(supporterId)
                .orElseThrow(() -> new RuntimeException("Worker not found: " + supporterId));

        if (!ticketEntity.canBeAssigned()) {
            throw new TicketCannotBeAssignedException(ticketId);
        }

        // связываем обе стороны
        workerEntity.assignNewTicket(ticketEntity);
        ticketEntity.assignTo(workerEntity);

        // явные дефолты (если нужно)
        if (ticketEntity.getTicketStatus() == null) {
            ticketEntity.setTicketStatus(TicketStatus.TODO);
        }
        if (ticketEntity.getUpdatedDate() == null) {
            ticketEntity.setUpdatedDate(Instant.now());
        }

        // Сохраняем. Порядок не критичен из-за каскада, но делаем оба для ясности
        ticketRepository.save(ticketEntity);
        workerRepository.save(workerEntity);

        return modelMapper.map(ticketEntity, ResponseTicketDto.class);
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
                : Sort.Direction.ASC;

        Sort sort = Sort.by(new Sort.Order(direction, sortField));
        pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<WorkerEntity> spec = genericSpecificationBuilder.buildFromRequest(request);
        Page<WorkerEntity> page = workerRepository.findAll(spec, pageable);
        CustomPage<WorkerEntity> customPage = CustomPage.from(page);

        // Используем ручной маппинг
        return customPage.map(this::mapWorkerToDto);
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

    @Override
    public WorkerAuthDto findByLogin(String login) {
        Specification<WorkerEntity> spec = WorkerSpecification.findLogin(login);
        WorkerEntity worker = workerRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Worker not found with login: " + login));

        return new WorkerAuthDto(
                worker.getId(),
                worker.getLogin(),
                worker.getPassword(), // теперь вернет "123456"
                worker.getRoles().stream()
                        .map(Role::toString)
                        .collect(Collectors.toList())
        );
    }

    private ResponseWorkerDto mapWorkerToDto(WorkerEntity worker) {
        ResponseWorkerDto dto = new ResponseWorkerDto();
        dto.setId(worker.getId());
        dto.setLogin(worker.getLogin());
        dto.setName(worker.getName());
        dto.setAssignedTickets(new HashSet<>()); // Пустой set для поиска
        return dto;
    }

}





//TODO AutoAssignTicket with TicketEvent



