package rent.vehicle.supportserviceapp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rent.vehicle.support.model.dto.CreateTicketDto;
import rent.vehicle.supportserviceapp.domain.SupporterEntity;

public interface SupporterRepository extends JpaRepository<SupporterEntity, Long> {
    @Query("SELECT s FROM SupporterEntity s ORDER BY SIZE(s.assignedTickets)ASC ")
    Page<SupporterEntity> findAllByOrderByAssignedTicketsAsc(Pageable pageable);
}
