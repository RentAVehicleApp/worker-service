package rent.vehicle.support.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Setter
@Getter
public class UpdateSupporterDto {
    private String login;
    private String name;
    private Set<CreateTicketDto> assignedTickets;
}
