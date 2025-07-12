package rent.vehicle.workerserviceapp.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rent.vehicle.workerserviceapp.service.WorkerClientService;

@Component
@RequiredArgsConstructor
public class TicketCreatedEventListener {
    final WorkerClientService workerClientService;
    @EventListener
    public void handleTicketCreatedEvent(TicketCreatedEvent ticketCreatedEvent) {
        workerClientService.autoAssignTicket(ticketCreatedEvent.getTicketId());
    }
}
