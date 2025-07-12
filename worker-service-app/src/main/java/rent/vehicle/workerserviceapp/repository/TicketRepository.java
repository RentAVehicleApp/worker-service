package rent.vehicle.workerserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<rent.vehicle.workerserviceapp.domain.TicketEntity,Long> {
}
