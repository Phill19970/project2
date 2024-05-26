package grid.capstone.mapper;

import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.model.MedicalRecord;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Javaughn Stephenson
 * @since 21/06/2023
 */

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecordMapper INSTANCE = Mappers.getMapper(MedicalRecordMapper.class);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "appointment.id", target = "appointmentId")
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    @InheritInverseConfiguration
    MedicalRecord toEntity(MedicalRecordDTO medicalRecordDTO);



}
