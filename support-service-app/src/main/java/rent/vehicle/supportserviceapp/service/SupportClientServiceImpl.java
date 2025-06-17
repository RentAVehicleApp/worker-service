package rent.vehicle.supportserviceapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.*;

@Service
@RequiredArgsConstructor
public class SupportClientServiceImpl implements SupportClientService {

    private final WebClient webClient;

    @Override
    public Mono<DeviceDto> createDevice(DeviceCreateUpdateDto deviceDto) {
        return webClient.post()
                .uri("v1/devices")
                .bodyValue(deviceDto)
                .retrieve()
                .bodyToMono(DeviceDto.class)
                ;
    }

    @Override
    public Mono<DeviceConfigDto> createDeviceConfig(DeviceConfigCreateUpdateDto deviceConfigDto) {
        return webClient.post()
                .uri("v1/deviceconfig")
                .bodyValue(deviceConfigDto)
                .retrieve()
                .bodyToMono(DeviceConfigDto.class)
                ;
    }

    @Override
    public Mono<Page<VehicleDto>> findAllVehicles(Pageable pageable) {
        return webClient.get()
                .uri("/v1/vehicles/list")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<VehicleDto>>() {
                });
    }
}
