package rent.vehicle.workerserviceapp.service.ticket;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import rent.vehicle.workerserviceapp.common.SearchCriteriaParser;
import rent.vehicle.workerserviceapp.domain.ticket.TicketEntity;
import rent.vehicle.workerserviceapp.event.TicketCreatedEventPublisher;
import rent.vehicle.workerserviceapp.repository.ticket.TicketRepository;
import rent.vehicle.workerserviceapp.service.specification.GenericSpecificationBuilder;
import rent.vehicle.workerservicemodel.common.common.CustomPage;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.ticket.CreateTicketDto;
import rent.vehicle.workerservicemodel.dto.ticket.ResponseTicketDto;
import rent.vehicle.workerservicemodel.dto.ticket.UpdateTicketDto;


@Service
@RequiredArgsConstructor
public class TicketClientServiceImpl implements TicketClientService {

    private final ModelMapper modelMapper;
    private final TicketRepository ticketRepository;
    private final TicketCreatedEventPublisher publisher;
    private final GenericSpecificationBuilder<TicketEntity> ticketSpecificationBuilder;
    private final SearchCriteriaParser searchCriteriaParser;


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
    public ResponseTicketDto closeTicket(Long id) {
        //TODO Add Security Validation for only supporters can close Tickets
        TicketEntity ticketEntity = ticketRepository.findById(id).get();
        ticketEntity.closeTicket();
        return modelMapper.map(ticketEntity,ResponseTicketDto.class);
    }

    @Override
    public void autoAssignTicket(Long ticketId) {

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
    public CustomPage<ResponseTicketDto> searchTickets(String  filter, Pageable pageable) {
        GenericSearchRequest request = searchCriteriaParser.buildSearchRequest(filter, pageable);
        String[] parts = request.getSort().split(",");
        //TODO поставить sort внутри спецификациибилдер
        String sortField = parts[0];
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1].trim())
                : Sort.Direction.ASC; // по умолчанию ASC, если не указано

        Sort sort = Sort.by(new Sort.Order(direction, sortField));

        pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<TicketEntity> spec = ticketSpecificationBuilder.buildFromRequest(request);
        Page<TicketEntity> page = ticketRepository.findAll(spec, pageable);
        CustomPage<TicketEntity> customPage = CustomPage.from(page);
        return customPage.map(entity -> modelMapper.map(entity, ResponseTicketDto.class));
    }


}