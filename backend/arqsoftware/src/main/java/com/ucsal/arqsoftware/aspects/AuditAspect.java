package com.ucsal.arqsoftware.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

import com.ucsal.arqsoftware.dto.PhysicalSpaceDTO;
import com.ucsal.arqsoftware.servicies.AuditService;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditService auditService;

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.UserService.insert(..))", returning = "user")
    public void logAfterUserInsert(JoinPoint joinPoint, Object user) {
        logAction(joinPoint, "Created", getResourceId(user));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.UserService.update(..))", returning = "user")
    public void logAfterUserUpdate(JoinPoint joinPoint, Object user) {
        logAction(joinPoint, "Updated", getResourceId(user));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.UserService.delete(..))", returning = "user")
    public void logAfterUserDelete(JoinPoint joinPoint, Object user) {
        logAction(joinPoint, "Deleted", getResourceId(user));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.PhysicalSpaceService.insert(..))", returning = "physicalSpace")
    public void logAfterPhysicalInsert(JoinPoint joinPoint, PhysicalSpaceDTO physicalSpace) {
        logAction(joinPoint, "Created", physicalSpace.getId());
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.PhysicalSpaceService.update(..))", returning = "physicalSpace")
    public void logAfterPhysicalUpdate(JoinPoint joinPoint, PhysicalSpaceDTO physicalSpace) {
        logAction(joinPoint, "Updated", physicalSpace.getId());
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.PhysicalSpaceService.delete(..))", returning = "physicalSpace")
    public void logAfterPhysicalDelete(JoinPoint joinPoint, PhysicalSpaceDTO physicalSpace) {
        logAction(joinPoint, "Deleted", physicalSpace.getId());
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.RequestService.insert(..))", returning = "request")
    public void logAfterRequestInsert(JoinPoint joinPoint, Object request) {
        logAction(joinPoint, "Created", getResourceId(request));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.RequestService.update(..))", returning = "request")
    public void logAfterRequestUpdate(JoinPoint joinPoint, Object request) {
        logAction(joinPoint, "Updated", getResourceId(request));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.RequestService.delete(..))", returning = "request")
    public void logAfterRequestDelete(JoinPoint joinPoint, Object request) {
        logAction(joinPoint, "Deleted", getResourceId(request));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.ApprovalHistoryService.insert(..))", returning = "approval")
    public void logAfterApprovalInsert(JoinPoint joinPoint, Object approval) {
        logAction(joinPoint, "Inserted", getResourceId(approval));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.ApprovalHistoryService.update(..))", returning = "approval")
    public void logAfterApprovalUpdate(JoinPoint joinPoint, Object approval) {
        logAction(joinPoint, "Modified", getResourceId(approval));
    }

    @AfterReturning(pointcut = "execution(* com.ucsal.arqsoftware.servicies.ApprovalHistoryService.delete(..))", returning = "approval")
    public void logAfterApprovalDelete(JoinPoint joinPoint, Object approval) {
        logAction(joinPoint, "Deleted", getResourceId(approval));
    }

    private void logAction(JoinPoint joinPoint, String action, Long resourceId) {
        String username = getLoggedUser();  
        String resourceType = getResourceType(joinPoint);
        String actionDescription = String.format("%s %s (ID: %s)", action, resourceType, resourceId);

        auditService.logAction(username, action, actionDescription); 
    }

    private String getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return getUsernameFromAuthentication(authentication);
        }
        return "Unknown User";
    }

    private String getUsernameFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaimAsString("username");
        }
        return authentication.getName();
    }

    private String getResourceType(JoinPoint joinPoint) {
        String serviceName = joinPoint.getSignature().getDeclaringTypeName();
        String serviceSimpleName = serviceName.substring(serviceName.lastIndexOf(".") + 1, serviceName.length() - "Service".length());

        switch (serviceSimpleName) {
            case "User":
                return "a user";
            case "PhysicalSpace":
                return "a physical space";
            case "Request":
                return "a request";
            case "ApprovalHistory":
                return "an approval";
            default:
                return "an unknown resource";
        }
    }

    private Long getResourceId(Object resource) {
        try {
            Method method = resource.getClass().getMethod("getId");
            Object id = method.invoke(resource);
            return id instanceof Long ? (Long) id : null; 
        } catch (NoSuchMethodException e) {
            return null;
        } catch (Exception ex) {
            System.out.println("Error obtaining ID: " + ex.getMessage());
            return null;
        }
    }
}
