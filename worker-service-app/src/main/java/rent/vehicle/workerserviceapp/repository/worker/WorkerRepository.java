package rent.vehicle.workerserviceapp.repository.worker;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import rent.vehicle.workerserviceapp.domain.worker.WorkerEntity;

public interface WorkerRepository extends JpaRepository<WorkerEntity, Long>, JpaSpecificationExecutor<WorkerEntity> {
    @Query("SELECT s FROM WorkerEntity s ORDER BY SIZE(s.assignedTickets)ASC ")
    Page<WorkerEntity> findAllByOrderByAssignedTicketsAsc(Pageable pageable);
}
