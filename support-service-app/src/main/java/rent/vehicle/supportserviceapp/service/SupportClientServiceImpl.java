package rent.vehicle.supportserviceapp.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
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
import rent.vehicle.supportserviceapp.repository.SupporterRepository;
import rent.vehicle.supportserviceapp.repository.TicketRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportClientServiceImpl implements SupportClientService {

    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final SupporterRepository supporterRepository;
    private final TicketRepository ticketRepository;


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
        return modelMapper.map(supporterEntity, CreateSupporterDto.class);
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
        return null;
    }

    @Override
    public Boolean removeSupporter(Long id) {
        return null;
    }

    @Override
    public CreateTicketDto createTicker(CreateTicketDto createTicketDto) {
        return null;
    }

    @Override
    public UpdateTicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto) {
        return null;
    }

    @Override
    public ResponseTicketDto findTicket(Long id) {
        return null;
    }

    @Override
    public Boolean removeTicket(Long id) {
        return null;
    }
    //TODO AutoAssignTicket with TicketEvent


}
