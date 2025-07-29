package rent.vehicle.workerservicemodel.exception;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(String message) {
        super(message);
    }
    public WorkerNotFoundException(Long workerId) {super(String.format("Worker with id %d not found", workerId));}
}
