package rent.vehicle.supportserviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.DeviceCreateUpdateDto;
import rent.vehicle.dto.DeviceDto;
import rent.vehicle.dto.ListDevicesRequest;

public interface DeviceService {

    Mono<DeviceDto> createDevice(DeviceCreateUpdateDto deviceCreateUpdateDto);

    Mono<DeviceDto> findDeviceById(long id);

    Mono<Page<DeviceDto>> findDevicesByParams(ListDevicesRequest listDevicesRequest, Pageable pageable);

    Mono<Page<DeviceDto>> findAllDevices(Pageable pageable);

    Mono<DeviceDto> updateDevice(long id, DeviceCreateUpdateDto deviceCreateUpdateDto);

    void removeDevice(long id);
}
