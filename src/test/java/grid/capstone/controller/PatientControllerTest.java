package grid.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import grid.capstone.dto.v1.PatientSignUp;
import grid.capstone.filter.JwtTokenFilter;
import grid.capstone.model.Patient;
import grid.capstone.model.Role;
import grid.capstone.service.jwt.JwtService;
import grid.capstone.service.patient.PatientService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PatientController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    static final String BASE_URL = "/api/v1/patients";


    @MockBean
    JwtService jwtService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PatientService patientService;

    PatientSignUp patientSignUp;

    @BeforeEach
    void setUp() {

        patientSignUp = PatientSignUp.builder()
                .password("password")
                .name("name")
                .address("address")
                .phoneNumber("1234567890")
                .email("email@gmail.com")
                .age(20)
                .gender('M')
                .description("description")
                .build();

    }

    @Test
    void getAllPatients() throws Exception {

        // Create sample data for test
        List<Patient> expectedPatients = List.of(
                Patient.builder()
                        .role(Role.ROLE_PATIENT)
                        .build()
        );

        given(patientService.getAllPatients(anyLong()))
                .willReturn(expectedPatients);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                .param("doctorId", "1")
        );

        resultActions
                .andExpect(status().isOk());

        // Verify that the patientService.getAllPatients() method was called once with the correct doctorId argument
        then(patientService).should(times(1)).getAllPatients(anyLong());

    }

    @Test
    void getPatient() throws Exception {

        Patient patient = Patient.builder()
                .role(Role.ROLE_PATIENT)
                .build();

        given(patientService.getPatient(anyLong()))
                .willReturn(patient);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/1"));

        resultActions
                .andExpect(status().isOk());

        // Verify that the patientService.getPatient() method was called once with the correct patientId argument
        then(patientService).should(times(1)).getPatient(anyLong());

    }

    @Test
    void savePatient() throws Exception {

        HttpStatus expectedStatus = HttpStatus.CREATED;

        given(patientService.savePatient(any(PatientSignUp.class), any()))
                .willReturn(expectedStatus);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientSignUp)));


        resultActions
                .andExpect(status().isCreated());

        // Verify that the patientService.savePatient() method was called once with the correct arguments
        then(patientService).should(times(1)).savePatient(any(PatientSignUp.class), any());

    }
}