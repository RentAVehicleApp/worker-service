package rent.vehicle.workerserviceapp.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import rent.vehicle.workerserviceapp.domain.ticket.TicketEntity;

@Component
public class TicketCreatedEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TicketCreatedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTicketCreatedEvent(TicketEntity ticketEntity) {
        TicketCreatedEvent ticketCreatedEvent = new TicketCreatedEvent(ticketEntity,ticketEntity.getId(),
                ticketEntity.getCreatedByUserId());
        applicationEventPublisher.publishEvent(ticketCreatedEvent);
    }
}
