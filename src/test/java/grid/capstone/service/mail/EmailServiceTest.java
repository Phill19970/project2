package grid.capstone.service.mail;

import grid.capstone.model.Appointment;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendAppointmentEmail() {
        Appointment appointment = new Appointment();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();

        emailService.sendAppointmentEmail(appointment, doctor, patient);

        then(mailSender).should(times(2)).send(any(SimpleMailMessage.class));
    }

}