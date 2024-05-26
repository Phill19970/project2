package grid.capstone.mapper;

import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Javaughn Stephenson
 * @since 19/06/2023
 */

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    Doctor toEntity(DoctorDTO doctorDTO);
    DoctorDTO toDTO(Doctor doctor);
    Doctor toEntity(DoctorSignUp doctorSignUp);


}
