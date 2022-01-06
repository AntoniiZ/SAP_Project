package com.company.SAP_Project.events;

import com.company.SAP_Project.models.User;
import com.company.SAP_Project.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.UUID;

@EnableAsync
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event)
    {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("SAP_Project account confirmation");
        email.setText(String.format(
                "Hello %s, please follow this link to activate your account: http://localhost:8080%s",
                user.getUsername(), String.format("%s/?token=%s", event.getAppUrl(), token)
        ));
        mailSender.send(email);
    }
}