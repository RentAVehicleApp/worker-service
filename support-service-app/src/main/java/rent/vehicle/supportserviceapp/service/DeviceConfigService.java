package rent.vehicle.supportserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.DeviceConfigCreateUpdateDto;
import rent.vehicle.dto.DeviceConfigDto;
import rent.vehicle.dto.ListDeviceConfigsRequest;

public interface DeviceConfigService {

    Mono<DeviceConfigCreateUpdateDto> createDeviceConfig(DeviceConfigCreateUpdateDto deviceConfigCreateUpdateDto);

    Mono<DeviceConfigCreateUpdateDto> updateDeviceConfig(long id, DeviceConfigCreateUpdateDto deviceConfigCreateUpdateDto);

    Mono<DeviceConfigDto> findDeviceConfigById(long id);

    Mono<Page<DeviceConfigDto>> findAllDeviceConfig(Pageable pageable);

    Mono<Page<DeviceConfigDto>> getListDevicesConfigByParam(ListDeviceConfigsRequest listDeviceConfigsRequest, Pageable pageable);

    Mono<Void> removeDeviceConfig(long id);
}
