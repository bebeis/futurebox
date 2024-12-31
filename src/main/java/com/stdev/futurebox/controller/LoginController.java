package com.stdev.futurebox.controller;

import com.stdev.futurebox.repository.LoginLogRepository;
import com.stdev.futurebox.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final LoginLogRepository loginLogRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       HttpServletRequest request,
                       RedirectAttributes redirectAttributes) {
        
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String userAgent = request.getHeader("User-Agent");
        
        boolean isSuccess = loginService.authenticate(username, password);
        loginLogRepository.saveLog(username, ipAddress, userAgent, isSuccess);
        log.info("login request from ip: {}, userAgent: {}, isSuccess: {}", ipAddress, userAgent, isSuccess);
        
        if (isSuccess) {
            session.setAttribute("loggedIn", true);
            return "redirect:/";
        }
        
        redirectAttributes.addAttribute("error", true);
        return "redirect:/login";
    }
} 