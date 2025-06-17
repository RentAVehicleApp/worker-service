package rent.vehicle.dto;

import rent.vehicle.enums.Availibility;
import rent.vehicle.enums.ConnectionStatus;
import rent.vehicle.enums.DeviceModel;
import rent.vehicle.enums.VehicleModel;

import java.awt.*;

public class VehicleDashboardDetailsDto {
    private long VehicleId; //Not visible on the dashboard
    private String registrationNumber;
    private VehicleModel vehicleModel;
    private long deviceId;
    private Availibility availibility;
    private Point point;
    int batteryStatus; // from 0 up to 100 %
    private String VehicleNodes;
    private long DeviceId; //Not visible on the dashboard
    private String serialNumber;
    private DeviceModel deviceModel;//??? Should this be displayed on the dashboard?
    private ConnectionStatus connectionStatus;
    private String nodes; //??? Should this be displayed on the dashboard?
    private long deviceConfigId;
    String deviceConfigName;
}
