package rent.vehicle.support.model.dto;

import rent.vehicle.support.model.enums.TicketStatus;

public class ResponseTicketDto {
    private Long id;
    private String createdByUserId;
    private String createdByUserName;
    private String header;
    private String problem;
    private TicketStatus status;
    private CreateSupporterDto assignedTo;
}
