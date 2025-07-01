package rent.vehicle.support.model.dto;

import lombok.*;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportWithTicketsDto {
        String login;
        Set<ResponseTicketDto> assignedTickets;
}
