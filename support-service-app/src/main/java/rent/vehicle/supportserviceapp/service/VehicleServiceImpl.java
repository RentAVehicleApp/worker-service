package rent.vehicle.supportserviceapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.ListVehiclesRequest;
import rent.vehicle.dto.VehicleCreateUpdateDto;
import rent.vehicle.dto.VehicleDto;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    @Override
    public Mono<VehicleDto> createVehicle(VehicleCreateUpdateDto vehicleCreateUpdateDto) {
        return null;
    }

    @Override
    public Mono<VehicleDto> findVehicleById(long id) {
        return null;
    }

    @Override
    public Mono<Page<VehicleDto>> findAllVehicles(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<Page<VehicleDto>> findVehicleByParams(ListVehiclesRequest listVehiclesRequest, Pageable pageable) {
        return null;
    }

    @Override
    public Mono<VehicleDto> updateVehicle(long id, VehicleCreateUpdateDto vehicleCreateUpdateDto) {
        return null;
    }

    @Override
    public Mono<Void> removeVehicle(long id) {
        return null;
    }
}
