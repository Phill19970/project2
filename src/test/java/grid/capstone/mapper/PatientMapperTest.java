package grid.capstone.mapper;

import grid.capstone.dto.v1.PatientSignUp;
import grid.capstone.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PatientMapperTest {

    PatientMapper patientMapper;

    @BeforeEach
    void setUp() {
        patientMapper = PatientMapper.INSTANCE;
    }

    @Test
    public void testToEntity() {
        // Arrange
        PatientSignUp patientSignUp = PatientSignUp.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .age(30)
                .bloodGroup("AB+")
                .religion("Christian")
                .occupation("Engineer")
                .gender('M')
                .maritalStatus("Single")
                .address("123 Main Street")
                .description("Some description")
                .password("password123")
                .build();

        // Act
        Patient patient = patientMapper.toEntity(patientSignUp);

        // Assert
        assertEquals("John Doe", patient.getName());
        assertEquals("john.doe@example.com", patient.getEmail());
        assertEquals("1234567890", patient.getPhoneNumber());
        assertEquals(30, patient.getAge());
        assertEquals("AB+", patient.getBloodGroup());
        assertEquals("Christian", patient.getReligion());
        assertEquals("Engineer", patient.getOccupation());
        assertEquals('M', patient.getGender());
        assertEquals("Single", patient.getMaritalStatus());
        assertEquals("123 Main Street", patient.getAddress());
        assertEquals("Some description", patient.getDescription());
        assertEquals("password123", patient.getPassword());
    }

    @Test
    public void testToDTO() {
        // Arrange
        Patient patient = Patient.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phoneNumber("9876543210")
                .age(25)
                .bloodGroup("O+")
                .religion("Muslim")
                .occupation("Teacher")
                .gender('F')
                .maritalStatus("Married")
                .address("456 Park Avenue")
                .description("Another description")
                .password("password456")
                .build();

        // Act
        PatientSignUp patientSignUp = patientMapper.toDTO(patient);

        // Assert
        assertEquals("Jane Smith", patientSignUp.getName());
        assertEquals("jane.smith@example.com", patientSignUp.getEmail());
        assertEquals("9876543210", patientSignUp.getPhoneNumber());
        assertEquals(25, patientSignUp.getAge());
        assertEquals("O+", patientSignUp.getBloodGroup());
        assertEquals("Muslim", patientSignUp.getReligion());
        assertEquals("Teacher", patientSignUp.getOccupation());
        assertEquals('F', patientSignUp.getGender());
        assertEquals("Married", patientSignUp.getMaritalStatus());
        assertEquals("456 Park Avenue", patientSignUp.getAddress());
        assertEquals("Another description", patientSignUp.getDescription());
        assertEquals("password456", patientSignUp.getPassword());
    }

    @Test
    public void testToEntity_NullInput() {
        // Arrange
        PatientMapper patientMapper = new PatientMapperImpl();
        PatientSignUp patientSignUp = null;

        // Act
        Patient patient = patientMapper.toEntity(patientSignUp);

        // Assert
        assertNull(patient);
    }

    @Test
    public void testToDTO_NullInput() {
        // Arrange
        PatientMapper patientMapper = new PatientMapperImpl();
        Patient patient = null;

        // Act
        PatientSignUp patientSignUp = patientMapper.toDTO(patient);

        // Assert
        assertNull(patientSignUp);
    }

}