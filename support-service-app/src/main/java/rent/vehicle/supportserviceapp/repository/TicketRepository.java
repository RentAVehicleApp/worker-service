package rent.vehicle.supportserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rent.vehicle.supportserviceapp.domain.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity,Long> {
}
