package grid.capstone.service.mail;

import grid.capstone.model.Appointment;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Javaughn Stephenson
 * @since 18/07/2023
 */

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;


    public void sendAppointmentEmail(Appointment appointment, Doctor doctor, Patient patient) {

        SimpleMailMessage doctorMail = new SimpleMailMessage();
        doctorMail.setFrom("capstone-mail@mail.com");
        doctorMail.setTo(doctor.getEmail());
        doctorMail.setSubject("Scheduled Appointment");
        doctorMail.setText("New appointment schedule with " + patient.getName()
                + " at " + appointment.getAppointmentDate() + " from " + appointment.getStartTime()
                + " to " + appointment.getEndTime());

        SimpleMailMessage patientMail = new SimpleMailMessage();
        patientMail.setFrom("capstone-mail@mail.com");
        patientMail.setTo(patient.getEmail());
        patientMail.setSubject("Scheduled Appointment");
        patientMail.setText("New appointment schedule with " + doctor.getName()
                + " at " + appointment.getAppointmentDate() + " from " + appointment.getStartTime()
                + " to " + appointment.getEndTime());


        mailSender.send(doctorMail);
        mailSender.send(patientMail);

    }

}
