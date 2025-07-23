package rent.vehicle.workerservicemodel.dto.specification;

import lombok.Getter;
import rent.vehicle.workerservicemodel.enums.Operations;
@Getter
public class SearchCriteria {
    String filter;
    Operations operation;
    String value;
}
