package rent.vehicle.workerserviceapp.service.specification;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import rent.vehicle.workerserviceapp.domain.worker.WorkerEntity;

public class WorkerSpecification {
    public static Specification<WorkerEntity>findLogin(String login) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("login"), login);
    }
}
