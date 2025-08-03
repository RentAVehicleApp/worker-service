package rent.vehicle.workerservicemodel.dto.worker;

import java.util.Collection;
import java.util.Set;

public record WorkerAuthDto(
        Long id,
        String login,
        Set<String> roles
) {}