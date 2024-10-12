package com.ucsal.arqsoftware.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.ucsal.arqsoftware.servicies.AuditService;

import jakarta.servlet.http.HttpServletRequest;

public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private AuditService auditService;
    
    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = request.getParameter("username");
        String action = "Login";
        String details = String.format("User '%s' logged in.", username);
        auditService.logAction(username, action, details);
    }
}