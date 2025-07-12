package rent.vehicle.workerserviceapp.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rent.vehicle.workerserviceapp.domain.TicketEntity;


public class TicketCreatedEvent extends ApplicationEvent {
    @Getter
    private final Long ticketId;
    private final Long userId;
    public TicketCreatedEvent(TicketEntity source, Long ticketId, Long userId) {
        super(source);
        this.ticketId = ticketId;
        this.userId = userId;
    }
}
