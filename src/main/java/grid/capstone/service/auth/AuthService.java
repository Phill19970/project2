package grid.capstone.service.auth;

import grid.capstone.dto.v1.AuthRequest;

/**
 * @author Javaughn Stephenson
 * @since 18/07/2023
 */

public interface AuthService {

    public String login(AuthRequest authRequest);

}
