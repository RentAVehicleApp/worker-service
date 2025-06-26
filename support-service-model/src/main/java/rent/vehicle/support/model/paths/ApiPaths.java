package rent.vehicle.support.model.paths;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public final class ApiPaths {
    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class");
    }
public static final  String USER = "api/v1/users";
public static final String USER_BY_EMAIL = "api/v1/users/email";
}
