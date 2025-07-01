package rent.vehicle.support.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateSupporterDto {
    private static final String password = "123456";
    private String login;
    private String name;
}
