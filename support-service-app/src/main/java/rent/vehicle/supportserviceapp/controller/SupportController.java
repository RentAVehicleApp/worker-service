package rent.vehicle.supportserviceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.*;
import rent.vehicle.supportserviceapp.service.SupportClientService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/support")
public class SupportController {
    private final SupportClientService supportClientService;

    @PostMapping("/device")
    public Mono<DeviceDto> createDeviceDto(@RequestBody DeviceCreateUpdateDto deviceCreateUpdateDto) {
        return supportClientService.createDevice(deviceCreateUpdateDto);
    }

    @PostMapping("/devconf")
    public Mono<DeviceConfigDto> createDeviceConfig(@RequestBody DeviceConfigCreateUpdateDto deviceConfigCreateUpdateDto) {
        System.out.println("createDeviceConfig");
        return supportClientService.createDeviceConfig(deviceConfigCreateUpdateDto);
    }

    @GetMapping("/vehicles/list")
    public Mono<Page<VehicleDto>> findAllVehicles(
            @PageableDefault(
                    size = 2
            )
            Pageable pageable) {
        return supportClientService.findAllVehicles(pageable);
    }


}
