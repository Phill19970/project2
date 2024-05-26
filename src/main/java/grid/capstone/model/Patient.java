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
@ToString(exclude = {"appointments"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Patient implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Age is required")
    private Integer age;

    private String bloodGroup;

    private String religion;

    private String occupation;

    @NotNull(message = "Gender is required")
    private Character gender;

    private String maritalStatus;

    @NotBlank(message = "Address is required")
    private String address;

    @Size(max = 100, message = "Description should be at most 100 characters")
    private String description;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private final Role role = Role.ROLE_PATIENT;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords;

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
