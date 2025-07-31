package rent.vehicle.workerservicemodel.dto.worker;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor // нужен для ModelMapper
@AllArgsConstructor // удобно строить вручную
public class WorkerSummaryDto {
    private Long id;
    private String login;
    private String name;
}