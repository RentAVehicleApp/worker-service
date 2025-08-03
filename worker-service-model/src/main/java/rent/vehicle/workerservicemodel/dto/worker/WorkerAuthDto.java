package rent.vehicle.workerservicemodel.dto.worker;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record WorkerAuthDto(
        Long id,
        String login,
        String password,
        List<String> roles
) {}