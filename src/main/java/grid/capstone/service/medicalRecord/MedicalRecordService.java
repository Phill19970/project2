package grid.capstone.service.medicalRecord;

import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.model.MedicalRecord;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 21/06/2023
 */

public interface MedicalRecordService {

    /**
     * Get all medical records associated to a patient
     *
     * @param patientId patient id
     * @return list of medical records
     */
    List<MedicalRecord> getMedicalRecords(Long patientId);

    /**
     * Creates a new medical record
     *
     * @param patientId patient id
     * @param medicalRecordDTO medical record DTO
     * @return HttpStatus
     */
    HttpStatus createMedicalRecord(Long patientId, MedicalRecordDTO medicalRecordDTO);
}
