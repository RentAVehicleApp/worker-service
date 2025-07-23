package rent.vehicle.workerserviceapp.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rent.vehicle.workerserviceapp.service.ticket.TicketClientService;

@Component
@RequiredArgsConstructor
public class TicketCreatedEventListener {
    final TicketClientService ticketClientService;
    @EventListener
    public void handleTicketCreatedEvent(TicketCreatedEvent ticketCreatedEvent) {
        ticketClientService.autoAssignTicket(ticketCreatedEvent.getTicketId());
    }
}
