package rent.vehicle.workerservicemodel.dto.worker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateWorkerDto {
    private String login;
    private String password;
    private String name;
    private Set<String> roles;
}
