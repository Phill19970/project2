package grid.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import grid.capstone.dto.v1.AppointmentDTO;
import grid.capstone.filter.JwtTokenFilter;
import grid.capstone.model.Appointment;
import grid.capstone.service.appointment.AppointmentService;
import grid.capstone.service.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    static final String BASE_URL = "/api/v1/appointments";


    @MockBean
    JwtService jwtService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AppointmentService appointmentService;

    Appointment appointment;
    AppointmentDTO appointmentDTO;

    @BeforeEach
    void setUp() {

        appointment = Appointment.builder().build();

        appointmentDTO = AppointmentDTO.builder()
                .doctorId(1L)
                .patientId(1L)
                .appointmentDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .reason("Reason")
                .build();

    }

    @Test
    void createAppointment() throws Exception {

        given(appointmentService.createAppointment(any(AppointmentDTO.class)))
                .willReturn(HttpStatus.CREATED);

        // Perform the POST request with the AppointmentDTO as the request body
        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)));

        resultActions
            .andExpect(status().isCreated());

    }

    @Test
    void getFilteredAppointments() throws Exception {

        List<Appointment> expectedAppointments = List.of(appointment);

        given(appointmentService.getFilteredAppointments(any(), any(), any()))
                .willReturn(expectedAppointments);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                .param("dateFilter", "2023-07-25") // Set query parameters as needed
                .param("patientId", "1")
                .param("doctorId", "1"));


        resultActions
                .andExpect(status().isOk());

        // Verify that the appointmentService.getFilteredAppointments() method was called once with the correct arguments
        then(appointmentService).should(times(1)).getFilteredAppointments(any(), any(), any());

    }

    @Test
    void updateAppointment() throws Exception {


        HttpStatus expectedStatus = HttpStatus.OK;

        given(appointmentService.updateAppointment(anyLong(), any(AppointmentDTO.class)))
                .willReturn(expectedStatus);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentDTO)));

        resultActions
                .andExpect(status().isOk());


        then(appointmentService).should(times(1)).updateAppointment(any(), any(AppointmentDTO.class));

    }
}