package grid.capstone.service.auth;

import grid.capstone.dto.v1.AuthRequest;
import grid.capstone.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author Javaughn Stephenson
 * @since 18/07/2023
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public String login(AuthRequest authRequest) {

        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getEmail(), authRequest.getPassword()
                        )
                );

        UserDetails user = userDetailsService.loadUserByUsername(authRequest.getEmail());

        return jwtService.generateToken(user);
    }
}
