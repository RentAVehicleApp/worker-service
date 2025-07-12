package rent.vehicle.workerserviceapp.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rent.vehicle.workerserviceapp.domain.WorkerEntity;

public interface WorkerRepository extends JpaRepository<WorkerEntity, Long> {
    @Query("SELECT s FROM WorkerEntity s ORDER BY SIZE(s.assignedTickets)ASC ")
    Page<WorkerEntity> findAllByOrderByAssignedTicketsAsc(Pageable pageable);
}
