package rent.vehicle.workerservicemodel.dto.specification;

import lombok.Getter;
import lombok.Setter;
import rent.vehicle.workerservicemodel.enums.Operations;
@Getter
@Setter
public class SearchCriteria {
    String filter;
    Operations operation;
    String value;
}
