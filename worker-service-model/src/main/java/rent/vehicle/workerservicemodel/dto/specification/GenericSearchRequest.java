package rent.vehicle.workerservicemodel.dto.specification;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericSearchRequest {
    private List<SearchCriteria> searchCriteriaList;
    private int page = 0;
    private int size = 20;
    private String sort = "id,desc";
}
