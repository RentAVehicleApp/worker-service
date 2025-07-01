package rent.vehicle.supportserviceapp.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rent.vehicle.supportserviceapp.service.SupportClientService;

@Component
@RequiredArgsConstructor
public class TicketCreatedEventListener {
    final SupportClientService supportClientService;
    @EventListener
    public void handleTicketCreatedEvent(TicketCreatedEvent ticketCreatedEvent) {
        supportClientService.autoAssignTicket(ticketCreatedEvent.getTicketId());
    }
}
