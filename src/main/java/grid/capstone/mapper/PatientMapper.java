package grid.capstone.mapper;

import grid.capstone.dto.v1.PatientSignUp;
import grid.capstone.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    Patient toEntity(PatientSignUp patientSignUp);

    PatientSignUp toDTO(Patient patient);
}
