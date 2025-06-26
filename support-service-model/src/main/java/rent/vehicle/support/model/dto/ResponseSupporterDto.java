package rent.vehicle.support.model.dto;

import java.util.Set;

public class ResponseSupporterDto {
    private Long id;
    private String login;
    private String name;
    private Set<ResponseTicketDto> assignedTickets;
}
