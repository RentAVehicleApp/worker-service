package rent.vehicle.supportserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.*;

public interface SupportClientService {


    Mono<DeviceDto> createDevice(DeviceCreateUpdateDto deviceDto);

    Mono<DeviceConfigDto> createDeviceConfig(DeviceConfigCreateUpdateDto deviceConfigDto);

    Mono<Page<VehicleDto>> findAllVehicles(Pageable pageable);
}
