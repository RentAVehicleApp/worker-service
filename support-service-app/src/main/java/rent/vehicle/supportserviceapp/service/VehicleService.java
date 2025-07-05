package rent.vehicle.supportserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.ListVehiclesRequest;
import rent.vehicle.dto.VehicleCreateUpdateDto;
import rent.vehicle.dto.VehicleDto;

public interface VehicleService {

    Mono<VehicleDto> createVehicle(VehicleCreateUpdateDto vehicleCreateUpdateDto);

    Mono<VehicleDto> findVehicleById(long id);

    Mono<Page<VehicleDto>> findAllVehicles(Pageable pageable);

    Mono<Page<VehicleDto>> findVehicleByParams(ListVehiclesRequest listVehiclesRequest, Pageable pageable);

    Mono<VehicleDto> updateVehicle(long id, VehicleCreateUpdateDto vehicleCreateUpdateDto);

    Mono<Void> removeVehicle(long id);
}
