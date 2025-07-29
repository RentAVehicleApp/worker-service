package rent.vehicle.workerserviceapp.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rent.vehicle.workerserviceapp.service.ticket.TicketClientService;
import rent.vehicle.workerserviceapp.service.worker.WorkerClientService;

@Component
@RequiredArgsConstructor
public class TicketCreatedEventListener {
    final TicketClientService ticketClientService;
    private final WorkerClientService workerClientService;

    @EventListener
    public void handleTicketCreatedEvent(TicketCreatedEvent ticketCreatedEvent) {
        workerClientService.autoAssignTicket(ticketCreatedEvent.getTicketId());
    }
}
