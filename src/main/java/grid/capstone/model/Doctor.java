package grid.capstone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Data
@ToString(exclude = {"availabilities"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Doctor implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    private String name;

    @Email(message = "Invalid email address")
    private String email;

    @JsonIgnore
    private String address;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String specialization;

    @NotNull(message = "Age is required")
    private Integer age;

    private String biography;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private final Role role = Role.ROLE_DOCTOR;

    @NotNull(message = "Skills list is required")
    @Size(min = 1, message = "Doctor's skills list must have at least one skill")
    private List<String> skills;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Availability> availabilities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
