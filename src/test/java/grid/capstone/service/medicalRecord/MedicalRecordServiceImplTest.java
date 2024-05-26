package grid.capstone.service.medicalRecord;

import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.MedicalRecordMapper;
import grid.capstone.mapper.MedicalRecordMapperImpl;
import grid.capstone.model.Appointment;
import grid.capstone.model.MedicalRecord;
import grid.capstone.model.Prescription;
import grid.capstone.repository.AppointmentRepository;
import grid.capstone.repository.MedicalRecordRepository;
import grid.capstone.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    MedicalRecordService medicalRecordService;

    @Mock
    MedicalRecordRepository medicalRepository;
    @Mock
    PatientRepository patientRepository;
    @Mock
    AppointmentRepository appointmentRepository;

    MedicalRecordMapper medicalMapper = new MedicalRecordMapperImpl();

    MedicalRecord testMedicalRecord;
    MedicalRecordDTO testMedicalDTO;
    Appointment testAppointment;


    @BeforeEach
    void setUp() {
        medicalRecordService = new MedicalRecordServiceImpl(medicalRepository, medicalMapper, patientRepository, appointmentRepository);

        testMedicalRecord = MedicalRecord.builder()
                .build();

        testAppointment = Appointment.builder()
                .id(1L)
                .build();

        testMedicalDTO = MedicalRecordDTO.builder()
                .appointmentId(1L)
                .prescriptions(List.of(
                        Prescription.builder()
                                .medication("Medication")
                                .dosage(1)
                                .total(BigDecimal.valueOf(123L))
                                .startDate(LocalDate.now())
                                .endDate(LocalDate.now())
                                .build()
                ))
                .build();

    }

    @Test
    void getMedicalRecords() {

        given(patientRepository.existsById(anyLong()))
                .willReturn(true);

        given(medicalRepository.findAllByPatientId(anyLong()))
                .willReturn(Arrays.asList(testMedicalRecord));

        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecords(1L);

        then(medicalRepository).should(times(1)).findAllByPatientId(1L);
        assertThat(medicalRecords).isEqualTo(List.of(testMedicalRecord));
    }

    @Test
    public void testGetMedicalRecordsThrowsResourceNotFoundException() {
        // Arrange
        Long nonExistentPatientId = 100L;
        given(patientRepository.existsById(nonExistentPatientId)).willReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            medicalRecordService.getMedicalRecords(nonExistentPatientId);
        });
    }

    @Test
    void getMedicalRecord_GivenWrongId_ThrowResourceError() {

        given(patientRepository.existsById(anyLong()))
                .willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> medicalRecordService.getMedicalRecords(1L));
        then(medicalRepository).should(times(0)).findAllByPatientId(anyLong());
    }

    @Test
    void createMedicalRecord() {
        given(appointmentRepository.findById(anyLong()))
                .willReturn(Optional.of(testAppointment));

        HttpStatus status = medicalRecordService.createMedicalRecord(1L, testMedicalDTO);

        then(medicalRepository).should(times(1)).save(any(MedicalRecord.class));
        then(appointmentRepository).should(times(1)).save(any(Appointment.class));
        assertThat(status).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testCreateMedicalRecordThrowsResourceNotFoundException() {
        // Arrange
        Long nonExistentAppointmentId = 100L;
        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setAppointmentId(2L);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAppointment(Appointment.builder()
                        .id(1L)
                .build());

        given(appointmentRepository.findById(anyLong())).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            medicalRecordService.createMedicalRecord(1L, medicalRecordDTO);
        });
    }
}