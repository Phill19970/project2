package grid.capstone.mapper;

import grid.capstone.dto.v1.AppointmentDTO;
import grid.capstone.model.Appointment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDTO toDTO(Appointment appointment);

    @InheritInverseConfiguration
    Appointment toEntity(AppointmentDTO appointmentDTO);

}
