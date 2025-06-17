package rent.vehicle.enums;

public enum DeviceModel {
    MODEL1, MODEL2, MODEL3;

    public static DeviceModel from(String value) {
        return value == null ? null : DeviceModel.valueOf(value.toUpperCase());
    }
}
