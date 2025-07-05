package rent.vehicle.supportserviceapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rent.vehicle.constants.ApiPaths;
import rent.vehicle.dto.DeviceConfigCreateUpdateDto;
import rent.vehicle.dto.DeviceConfigDto;
import rent.vehicle.dto.ListDeviceConfigsRequest;
import rent.vehicle.supportserviceapp.config.QueryParamUtil;

@Service
@RequiredArgsConstructor
public class DeviceConfigServiceImpl implements DeviceConfigService {

    private final WebClient webClient;

    @Override
    public Mono<DeviceConfigCreateUpdateDto> createDeviceConfig(DeviceConfigCreateUpdateDto deviceConfigCreateUpdateDto) {
        return webClient.post()
                .uri(ApiPaths.PATH_DEVICE_CONFIG)
                .bodyValue(deviceConfigCreateUpdateDto)
                .retrieve()
                .bodyToMono(DeviceConfigCreateUpdateDto.class);
    }

    @Override
    public Mono<DeviceConfigCreateUpdateDto> updateDeviceConfig(long id, DeviceConfigCreateUpdateDto deviceConfigCreateUpdateDto) {
        return webClient.put()
                .uri(ApiPaths.PATH_DEVICE_CONFIG + ApiPaths.PATH_ID, id)
                .bodyValue(deviceConfigCreateUpdateDto)
                .retrieve()
                .bodyToMono(DeviceConfigCreateUpdateDto.class);
    }

    @Override
    public Mono<DeviceConfigDto> findDeviceConfigById(long id) {
        return webClient.post()
                .uri(ApiPaths.PATH_DEVICE_CONFIG + ApiPaths.PATH_ID, id)
                .retrieve()
                .bodyToMono(DeviceConfigDto.class);
    }

    @Override
    public Mono<Page<DeviceConfigDto>> findAllDeviceConfig(Pageable pageable) {
        MultiValueMap<String, String> queryParams = QueryParamUtil.convertToQueryParams(null, pageable);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ApiPaths.PATH_DEVICE_CONFIG + ApiPaths.PATH_LIST)
                        .queryParams(queryParams)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<DeviceConfigDto>>() {
                });
    }

    @Override
    public Mono<Page<DeviceConfigDto>> getListDevicesConfigByParam(ListDeviceConfigsRequest listDeviceConfigsRequest, Pageable pageable) {
        MultiValueMap<String, String> queryParams = QueryParamUtil.convertToQueryParams(listDeviceConfigsRequest, pageable);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ApiPaths.PATH_DEVICE_CONFIG + ApiPaths.PATH_SEARCH)
                        .queryParams(queryParams)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<DeviceConfigDto>>() {
                });
    }

    @Override
    public Mono<Void> removeDeviceConfig(long id) {
        return webClient.delete()
                .uri(ApiPaths.PATH_DEVICE_CONFIG + ApiPaths.PATH_ID, id) // /api/device-config/{id}
                .retrieve()
                .bodyToMono(Void.class);
    }


}
