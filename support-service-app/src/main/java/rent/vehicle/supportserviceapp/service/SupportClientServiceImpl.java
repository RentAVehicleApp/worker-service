package rent.vehicle.supportserviceapp.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.*;
import rent.vehicle.dto.request.CreateUserDto;
import rent.vehicle.support.model.dto.*;
import rent.vehicle.support.model.paths.ApiPaths;
import rent.vehicle.supportserviceapp.config.SupportUserConfiguration;
import rent.vehicle.supportserviceapp.domain.SupporterEntity;
import rent.vehicle.supportserviceapp.domain.TicketEntity;
import rent.vehicle.supportserviceapp.event.TicketCreatedEventPublisher;
import rent.vehicle.supportserviceapp.repository.SupporterRepository;
import rent.vehicle.supportserviceapp.repository.TicketRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportClientServiceImpl implements SupportClientService {

    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final SupporterRepository supporterRepository;
    private final TicketRepository ticketRepository;
    private final TicketCreatedEventPublisher publisher;


    @Override
    public Mono<UserDto> createUser(CreateUserDto createUserDto) {
        return webClient.get()
                .uri(ApiPaths.USER_BY_EMAIL+"/"+createUserDto.getEmail())
                .retrieve()
                .bodyToMono(UserDto.class)
                ;
    }

    @Override
    public ResponseSupporterDto createSupporter(CreateSupporterDto createSupporterDto) {
        SupporterEntity supporterEntity = modelMapper.map(createSupporterDto, SupporterEntity.class);
        supporterRepository.save(supporterEntity);
        return modelMapper.map(supporterEntity, ResponseSupporterDto.class);
    }

    @Override
    public ResponseSupporterDto updateSupporter(Long id, UpdateSupporterDto updateSupporterDto) {
       Optional<SupporterEntity> supporterEntity = supporterRepository.findById(id);
       if (supporterEntity.isPresent()) {
           if (updateSupporterDto.getLogin() != null && !updateSupporterDto.getLogin().isEmpty()) {
               supporterEntity.get().setLogin(updateSupporterDto.getLogin());
           }
           if (updateSupporterDto.getName() != null && !updateSupporterDto.getName().isEmpty()) {
               supporterEntity.get().setName(updateSupporterDto.getName());
           }
           if (updateSupporterDto.getAssignedTickets() != null && !updateSupporterDto.getAssignedTickets().isEmpty()) {
               supporterEntity.get().setAssignedTickets(updateSupporterDto.getAssignedTickets().stream()
                       .map(t->modelMapper.map(t, TicketEntity.class))
                       .collect(Collectors.toSet()));
           }
           supporterRepository.save(supporterEntity.get());
           return modelMapper.map(supporterEntity.get(),ResponseSupporterDto.class);
       }
     return null;
    }

    @Override
    public ResponseSupporterDto findSupporter(Long id) {
        SupporterEntity supporterEntity = supporterRepository.findById(id).isPresent()?supporterRepository.findById(id).get():null;
        if(supporterEntity==null){
            throw new SupporterNotFoundException();
        }
        return modelMapper.map(supporterEntity,ResponseSupporterDto.class);
    }

    @Override
    public Boolean removeSupporter(Long id) {
       if(supporterRepository.findById(id).isPresent()){
           supporterRepository.deleteById(id);
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
        SupporterEntity supporterEntity = supporterRepository.findById(supporterId).get();
        if(supporterEntity==null||ticketEntity==null){
            throw new RuntimeException();
        }
        if(ticketEntity.canBeAssigned()){
            supporterEntity.assignNewTicket(ticketEntity);
        }else{
            throw new TicketCannotBeAssignedException();
        }
        ticketEntity.assignTo(supporterEntity);
        ticketRepository.save(ticketEntity);
        supporterRepository.save(supporterEntity);
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
        if(supporterRepository.findById(toSupporterId).isPresent()&&ticketRepository.findById(ticketId).isPresent()){
            TicketEntity ticket = ticketRepository.findById(ticketId).get();
            SupporterEntity assignToSupporter = supporterRepository.findById(toSupporterId).get();
            SupporterEntity reassignFromSupporterEntity = supporterRepository.findById(ticket.getAssignedTo().getId()).get();
            reassignFromSupporterEntity.getAssignedTickets().remove(ticket);
            if(ticket.canBeAssigned()){
                assignToSupporter.assignNewTicket(ticket);
                ticket.assignTo(assignToSupporter);
                ticketRepository.save(ticket);
                supporterRepository.save(assignToSupporter);
                supporterRepository.save(reassignFromSupporterEntity);
                return modelMapper.map(ticket,ResponseTicketDto.class);
            }
        }
        return null;
    }
//TODO decide if neeeded?
    @Override
    public int getSupporterWorkload(Long id) {
        if(!supporterRepository.findById(id).isPresent()){
          throw new SupportNotFoundException();
        }
        SupporterEntity supporterEntity = supporterRepository.findById(id).get();
        return supporterEntity.getAssignedTickets().size();
    }

    @Override
    public Page<ResponseTicketDto> getAllTickets() {
        //TODO Add Security Validation for only supporters can get all Tickets
        Pageable pageable = PageRequest.of(0, 10);
        Page<TicketEntity> allTickets = ticketRepository.findAll(pageable);
        return allTickets.map(ticket->modelMapper.map(ticket,ResponseTicketDto.class));
    }

    @Override
    public Page<SupportWithTicketsDto> getAllSupporters(Pageable pageable) {


        return supporterRepository.findAll(pageable)
                .map(supporterEntity ->{
                    SupportWithTicketsDto supportWithTicketsDto = new SupportWithTicketsDto();
                    supportWithTicketsDto.setLogin(supporterEntity.getLogin());
                    supportWithTicketsDto.setAssignedTickets(supporterEntity.getAssignedTickets().stream()
                            .map(assignedTicketEntity -> modelMapper.map(assignedTicketEntity, ResponseTicketDto.class))
                            .collect(Collectors.toSet()));
                    return supportWithTicketsDto;
                });
    }

    @Override
    public void autoAssignTicket(Long ticketId) {
    Page<SupporterEntity> allSupporters = supporterRepository.findAllByOrderByAssignedTicketsAsc(PageRequest.of(0, 1));
    if(allSupporters.hasContent()){
        SupporterEntity supporterEntity = allSupporters.getContent().get(0);
        assignTicket(supporterEntity.getId(), ticketId);
    }else{
        throw new TicketCannotBeAssignedException();
    }

    }
    //TODO AutoAssignTicket with TicketEvent


}
