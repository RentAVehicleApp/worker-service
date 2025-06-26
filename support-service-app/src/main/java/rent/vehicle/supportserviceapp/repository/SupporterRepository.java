package rent.vehicle.supportserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rent.vehicle.support.model.dto.CreateTicketDto;
import rent.vehicle.supportserviceapp.domain.SupporterEntity;

public interface SupporterRepository extends JpaRepository<SupporterEntity, Long> {
}
