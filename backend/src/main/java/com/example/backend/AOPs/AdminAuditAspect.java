package com.example.backend.AOPs;

import com.example.backend.Models.AuditLog;
import com.example.backend.Models.User;
import com.example.backend.Repositories.AuditLogRepository;
import com.example.backend.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AdminAuditAspect {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterReturning(pointcut = "@annotation(auditAction)", returning = "result")
    public void logAuditAction(JoinPoint joinPoint, AuditAction auditAction, Object result) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

            String ipAddress = request != null ? request.getRemoteAddr() : "UNKNOWN";
            String email = request != null && request.getParameter("email") != null
                    ? request.getParameter("email")
                    : "admin@campushire.com"; // Fallback email

            User actor = userRepository.findByEmail(email).orElse(null);
            if (actor == null) {
                System.err.println("AuditLog ignored: User not found for email " + email);
                return;
            }

            // Attempt to extract Target Entity ID from method arguments (assuming typical
            // pattern where ID is passed)
            Long targetEntityId = null;
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof Long) {
                    targetEntityId = (Long) arg;
                    break;
                }
            }

            String details = "Action executed successfully: " + joinPoint.getSignature().getName();

            AuditLog log = AuditLog.builder()
                    .actor(actor)
                    .action(auditAction.action())
                    .targetEntity(auditAction.targetEntity())
                    .targetEntityId(targetEntityId)
                    .details(details)
                    .timestamp(LocalDateTime.now())
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to log audit action: " + e.getMessage());
        }
    }
}
