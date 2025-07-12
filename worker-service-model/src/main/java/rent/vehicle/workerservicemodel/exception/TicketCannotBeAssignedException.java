package rent.vehicle.workerservicemodel.exception;

public class TicketCannotBeAssignedException extends RuntimeException {
    public TicketCannotBeAssignedException(Long ticketId) {

        super(String.format("Ticket with id %s cannot be assigned", ticketId));
    }
}
