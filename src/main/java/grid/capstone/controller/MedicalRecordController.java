package grid.capstone.controller;

import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.model.MedicalRecord;
import grid.capstone.service.medicalRecord.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 21/06/2023
 */

@RestController
@RequestMapping("/api/v1/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalService;

    public MedicalRecordController(MedicalRecordService medicalService) {
        this.medicalService = medicalService;
    }

    @GetMapping("/{patientId}")
    public List<MedicalRecord> getMedicalRecords(@PathVariable Long patientId) {
        return medicalService.getMedicalRecords(patientId);
    }

    @PostMapping("/{patientId}")
    public ResponseEntity<HttpStatus> createMedicalRecord(
            @PathVariable Long patientId,
            @Valid @RequestBody MedicalRecordDTO medicalRecordDTO
    ) {

        return ResponseEntity
                .status(medicalService.createMedicalRecord(patientId, medicalRecordDTO))
                .build();
    }
}
