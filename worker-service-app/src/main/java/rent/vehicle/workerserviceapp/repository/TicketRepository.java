package rent.vehicle.workerserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rent.vehicle.workerserviceapp.domain.TicketEntity;


public interface TicketRepository extends JpaRepository<TicketEntity,Long>, JpaSpecificationExecutor<TicketEntity> {
}
