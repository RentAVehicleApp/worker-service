package rent.vehicle.support.model.dto;

import rent.vehicle.support.model.enums.UserLicenseType;
import rent.vehicle.support.model.enums.UserRole;
import rent.vehicle.support.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;  // Изменено с userId на id
    private String firstName;  // Изменено с userFirstName
    private String lastName;   // Изменено с userLastName
    private String email;      // Изменено с userEmail
    private String phoneNumber; // Изменено с userPhoneNumber
    private LocalDate birthDate; // Изменено с userBirthDate
    private UserLicenseType licenseType; // Изменено с userLicense
    private String drivingLicenseNumber; // Оставлено как есть
    private Instant createdAt;
    private Instant updatedAt;
    private UserStatus status; // Добавьте статус
    private List<UserRole> roles; // Добавьте роли
}