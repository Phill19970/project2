package grid.capstone.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import grid.capstone.dto.v1.MedicalRecordDTO;
import grid.capstone.filter.JwtTokenFilter;
import grid.capstone.model.MedicalRecord;
import grid.capstone.service.jwt.JwtService;
import grid.capstone.service.medicalRecord.MedicalRecordService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class MedicalRecordControllerTest {


    static final String BASE_URL = "/api/v1/medical-record";


    @MockBean
    JwtService jwtService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MedicalRecordService medicalRecordService;

    MedicalRecord medicalRecord;
    MedicalRecordDTO medicalRecordDTO;

    @BeforeEach
    void setUp() {

        medicalRecord = MedicalRecord.builder()
                .checkInDate(LocalDate.now())
                .roomNo("5B")
                .build();

        medicalRecordDTO = MedicalRecordDTO.builder()
                .checkInDate(LocalDate.now())
                .appointmentId(1L)
                .doctorId(1L)
                .patientId(1L)
                .roomNo("4B")
                .build();

    }

    @Test
    void getMedicalRecords() throws Exception {

        given(medicalRecordService.getMedicalRecords(anyLong()))
                .willReturn(List.of(medicalRecord));

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL + "/1")
        );

        //then
        resultActions
                .andExpect(status().isOk());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        List<MedicalRecord> medicalRecords = objectMapper.readValue(content, new TypeReference<List<MedicalRecord>>() {
        });

        assertThat(medicalRecords).isEqualTo(List.of(medicalRecord));
    }

    @Test
    void createMedicalRecord() throws Exception{

        given(medicalRecordService.createMedicalRecord(anyLong(), any(MedicalRecordDTO.class)))
                .willReturn(HttpStatus.CREATED);

        ResultActions resultActions = mockMvc.perform(
                post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordDTO))
        );

        //then
        resultActions
                .andExpect(status().isCreated());

    }
}