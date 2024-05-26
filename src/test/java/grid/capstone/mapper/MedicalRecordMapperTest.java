package grid.capstone.mapper;

import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MedicalRecordMapperTest {

    MedicalRecordMapper medicalRecordMapper;

    @BeforeEach
    void setUp() {
        medicalRecordMapper = MedicalRecordMapper.INSTANCE;
    }

    @Test
    public void testToDTO() {

        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Appointment appointment = Appointment.builder().id(3L).build();

        List<Prescription> prescriptions = new ArrayList<>();
        Prescription prescription1 = Prescription.builder().id(101L).build();
        Prescription prescription2 = Prescription.builder().id(102L).build();
        prescriptions.add(prescription1);
        prescriptions.add(prescription2);

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .doctor(doctor)
                .patient(patient)
                .appointment(appointment)
                .checkInDate(LocalDate.now())
                .notes("Some notes")
                .disease("Fever")
                .status("In Progress")
                .roomNo("101")
                .prescriptions(prescriptions)
                .build();

        // Act
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDTO(medicalRecord);

        // Assert
        assertEquals(1L, medicalRecordDTO.getDoctorId());
        assertEquals(2L, medicalRecordDTO.getPatientId());
        assertEquals(3L, medicalRecordDTO.getAppointmentId());
        assertEquals(LocalDate.now(), medicalRecordDTO.getCheckInDate());
        assertEquals("Some notes", medicalRecordDTO.getNotes());
        assertEquals("Fever", medicalRecordDTO.getDisease());
        assertEquals("In Progress", medicalRecordDTO.getStatus());
        assertEquals("101", medicalRecordDTO.getRoomNo());
        assertEquals(2, medicalRecordDTO.getPrescriptions().size());
        assertEquals(101L, medicalRecordDTO.getPrescriptions().get(0).getId());
        assertEquals(102L, medicalRecordDTO.getPrescriptions().get(1).getId());
    }

    @Test
    public void testToEntity() {
        MedicalRecordDTO medicalRecordDTO = MedicalRecordDTO.builder()
                .doctorId(1L)
                .patientId(2L)
                .appointmentId(3L)
                .checkInDate(LocalDate.now())
                .notes("Some notes")
                .disease("Fever")
                .status("In Progress")
                .roomNo("101")
                .build();

        // Act
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);

        // Assert
        assertEquals(1L, medicalRecord.getDoctor().getId());
        assertEquals(2L, medicalRecord.getPatient().getId());
        assertEquals(3L, medicalRecord.getAppointment().getId());
        assertEquals(LocalDate.now(), medicalRecord.getCheckInDate());
        assertEquals("Some notes", medicalRecord.getNotes());
        assertEquals("Fever", medicalRecord.getDisease());
        assertEquals("In Progress", medicalRecord.getStatus());
        assertEquals("101", medicalRecord.getRoomNo());
    }

    @Test
    public void testToDTO_NullInput() {
        // Arrange
        MedicalRecordMapper medicalRecordMapper = new MedicalRecordMapperImpl();
        MedicalRecord medicalRecord = null;

        // Act
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDTO(medicalRecord);

        // Assert
        assertNull(medicalRecordDTO);
    }

    @Test
    public void testToEntity_NullInput() {
        // Arrange
        MedicalRecordMapper medicalRecordMapper = new MedicalRecordMapperImpl();
        MedicalRecordDTO medicalRecordDTO = null;

        // Act
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);

        // Assert
        assertNull(medicalRecord);
    }


}