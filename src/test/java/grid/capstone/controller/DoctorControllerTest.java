package grid.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.filter.JwtTokenFilter;
import grid.capstone.model.Doctor;
import grid.capstone.model.Role;
import grid.capstone.service.doctor.DoctorService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DoctorController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    static final String BASE_URL = "/api/v1/doctors";


    @MockBean
    JwtService jwtService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DoctorService doctorService;

    DoctorSignUp doctorSignUp;
    Doctor doctor;

    DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {

        doctorSignUp = DoctorSignUp.builder()
                .password("password")
                .name("name")
                .address("address")
                .phoneNumber("1234567890")
                .email("email@gmail.com")
                .age(20)
                .gender('M')
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .name("name")
                .role(Role.ROLE_PATIENT)
                .build();

        doctorDTO = DoctorDTO.builder()
                .name("name")
                .department("department")
                .biography("Bio")
                .build();

    }

    @Test
    void getAllDoctors() throws Exception {

        List<DoctorDTO> expectedDoctors = List.of(doctorDTO);

        // Mock the doctorService.getAllDoctors() method
        given(doctorService.getAllDoctors(any(), any(), any(), anyInt(), anyInt()))
                .willReturn(new PageImpl<>(expectedDoctors));

        // Perform the GET request to the getAllDoctors endpoint with optional query parameters
        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                .param("specialization", "Dentist")
                .param("department", "Cardiology")
                .param("name", "John")
                .param("size", "10")
                .param("page", "0"));


        resultActions
                .andExpect(status().isOk()) // Assert the expected HTTP status
                .andExpect(jsonPath("$.content").isArray()) // Assert the response content is an array
                .andExpect(jsonPath("$.content.length()").value(expectedDoctors.size())); // Assert the response content length

        // Verify that the doctorService.getAllDoctors() method was called once with the correct arguments
        then(doctorService).should(times(1)).getAllDoctors(any(), any(), any(), anyInt(), anyInt());

    }

    @Test
    void saveDoctor() throws Exception {

        HttpStatus expectedStatus = HttpStatus.CREATED;

        given(doctorService.saveDoctor(any(DoctorSignUp.class)))
                .willReturn(expectedStatus);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorSignUp)));

        resultActions
                .andExpect(status().isCreated());

        // Verify that the doctorService.saveDoctor() method was called once with the correct DoctorSignUp argument
        then(doctorService).should(times(1)).saveDoctor(any(DoctorSignUp.class));

    }

    @Test
    void getDoctor() throws Exception {

        given(doctorService.getDoctor(anyLong()))
                .willReturn(doctor);

        // Perform the GET request to the getDoctor endpoint with the doctorId parameter
        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctor.getId()))
                .andExpect(jsonPath("$.name").value(doctor.getName()));

        // Verify that the doctorService.getDoctor() method was called once with the correct doctorId argument
        then(doctorService).should(times(1)).getDoctor(anyLong());

    }
}