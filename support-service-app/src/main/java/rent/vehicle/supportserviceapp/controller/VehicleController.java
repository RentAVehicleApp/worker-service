package rent.vehicle.supportserviceapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import rent.vehicle.dto.ListVehiclesRequest;
import rent.vehicle.dto.VehicleCreateUpdateDto;
import rent.vehicle.dto.VehicleDto;
import rent.vehicle.supportserviceapp.service.VehicleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public Mono<VehicleDto> createVehicle(@RequestBody VehicleCreateUpdateDto vehicleCreateUpdateDto) {
        return vehicleService.createVehicle(vehicleCreateUpdateDto);
    }

    @GetMapping("/{id}")
    public Mono<VehicleDto> findVehicleById(@PathVariable long id) {
        return vehicleService.findVehicleById(id);
    }

    @GetMapping("/list")
    public Mono<Page<VehicleDto>> findAllVehicles(
            @PageableDefault(
                    size = 2,
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {
        return vehicleService.findAllVehicles(pageable);
    }

    @GetMapping("/search")
    public Mono<Page<VehicleDto>> findVehicleByParams(
            @ModelAttribute ListVehiclesRequest listVehiclesRequest,
            @PageableDefault(
                    size = 2,
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return vehicleService.findVehicleByParams(listVehiclesRequest, pageable);
    }

    @PutMapping("/{id}")
    public Mono<VehicleDto> updateVehicle(@PathVariable long id, @RequestBody VehicleCreateUpdateDto vehicleCreateUpdateDto) {
        return vehicleService.updateVehicle(id, vehicleCreateUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void removeVehicle(@PathVariable long id) {
        vehicleService.removeVehicle(id);
    }

}

