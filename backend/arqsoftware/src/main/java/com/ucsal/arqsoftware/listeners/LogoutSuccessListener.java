package com.ucsal.arqsoftware.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import com.ucsal.arqsoftware.servicies.AuditService;

import jakarta.servlet.http.HttpServletRequest;

public class LogoutSuccessListener implements ApplicationListener<LogoutSuccessEvent> {

	@Autowired
    private AuditService auditService;
	
	@Autowired
	private HttpServletRequest request;

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
    	String username = request.getParameter("username");
        String action = "Logout";
        String details = String.format("User '%s' logged out.", username);
        auditService.logAction(username, action, details);
    }
}
