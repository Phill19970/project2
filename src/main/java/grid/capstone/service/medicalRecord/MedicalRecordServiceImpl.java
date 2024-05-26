package grid.capstone.service.medicalRecord;

import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.MedicalRecordMapper;
import grid.capstone.model.Appointment;
import grid.capstone.model.MedicalRecord;
import grid.capstone.repository.AppointmentRepository;
import grid.capstone.repository.MedicalRecordRepository;
import grid.capstone.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 21/06/2023
 */

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRepository;
    private final MedicalRecordMapper medicalMapper;

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRepository, MedicalRecordMapper medicalMapper, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.medicalRepository = medicalRepository;
        this.medicalMapper = medicalMapper;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }


    @Override
    public List<MedicalRecord> getMedicalRecords(Long patientId) {
        //TODO: Throw Exception when id doesnt exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient with id " + patientId + " does not exist");
        }

        return medicalRepository.findAllByPatientId(
                patientId
        );
    }

    @Override
    @Transactional
    public HttpStatus createMedicalRecord(Long patientId, MedicalRecordDTO medicalRecordDTO) {

        //Change the DTO to the entity
        MedicalRecord medicalRecord = medicalMapper.toEntity(medicalRecordDTO);

        //TODO: Throw Exception when id doesnt exists
        //Get the appointment id from the entity passed and look it up in the DB
        Appointment appointment = appointmentRepository.findById(medicalRecord.getAppointment().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment with id " + medicalRecord.getAppointment().getId()  + " does not exist")
                );


        //Update the relationship of the prescriptions
        medicalRecord.getPrescriptions()
                .forEach(prescription -> prescription.setMedicalRecord(medicalRecord));

        MedicalRecord savedMedicalRecord = medicalRepository.save(medicalRecord);


        //Update the appointment record with the medical record
        appointment.setMedicalRecord(savedMedicalRecord);
        appointmentRepository.save(appointment);

        return HttpStatus.CREATED;
    }
}
