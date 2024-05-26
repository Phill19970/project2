package grid.capstone.mapper;

import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DoctorMapperTest {

    DoctorMapper doctorMapper;

    @BeforeEach
    void setUp() {
        doctorMapper = DoctorMapper.INSTANCE;
    }

    @Test
    public void testToEntity() {
        // Arrange
        DoctorDTO doctorDTO = DoctorDTO.builder()
                .id(1L)
                .name("John Doe")
                .biography("Experienced doctor")
                .build();

        // Act
        Doctor doctor = doctorMapper.toEntity(doctorDTO);

        // Assert
        assertEquals(1L, doctor.getId());
        assertEquals("John Doe", doctor.getName());
        assertEquals("Experienced doctor", doctor.getBiography());
    }

    @Test
    public void testToDTO() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("John Doe")
                .biography("Experienced doctor")
                .build();

        // Act
        DoctorDTO doctorDTO = doctorMapper.toDTO(doctor);

        // Assert
        assertEquals(1L, doctorDTO.getId());
        assertEquals("John Doe", doctorDTO.getName());
        assertEquals("Experienced doctor", doctorDTO.getBiography());
    }

    @Test
    public void testToEntityFromSignUp() {
        // Arrange
        DoctorSignUp doctorSignUp = DoctorSignUp.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .address("123 Main St")
                .phoneNumber("555-1234")
                .specialization("Cardiology")
                .age(35)
                .biography("Cardiologist with extensive experience")
                .password("password")
                .skills(List.of("Communication", "Leadership", "Problem-solving"))
                .build();

        // Act
        Doctor doctor = doctorMapper.toEntity(doctorSignUp);

        // Assert
        assertEquals("Jane Smith", doctor.getName());
        assertEquals("jane.smith@example.com", doctor.getEmail());
        assertEquals("123 Main St", doctor.getAddress());
        assertEquals("555-1234", doctor.getPhoneNumber());
        assertEquals("Cardiology", doctor.getSpecialization());
        assertEquals(35, doctor.getAge());
        assertEquals("Cardiologist with extensive experience", doctor.getBiography());
        assertEquals("password", doctor.getPassword());
        List<String> expectedSkills = List.of("Communication", "Leadership", "Problem-solving");
        assertEquals(expectedSkills, doctor.getSkills());
    }

    @Test
    public void testToEntity_NullInput() {
        // Arrange
        DoctorDTO doctorDTO = null;

        // Act
        Doctor doctor = doctorMapper.toEntity(doctorDTO);

        // Assert
        assertNull(doctor);
    }

    @Test
    public void testToDTO_NullInput() {
        // Arrange
        Doctor doctor = null;

        // Act
        DoctorDTO doctorDTO = doctorMapper.toDTO(doctor);

        // Assert
        assertNull(doctorDTO);
    }

    @Test
    public void testToEntityFromSignUp_NullInput() {
        // Arrange
        DoctorSignUp doctorSignUp = null;

        // Act
        Doctor doctor = doctorMapper.toEntity(doctorSignUp);

        // Assert
        assertNull(doctor);
    }
}