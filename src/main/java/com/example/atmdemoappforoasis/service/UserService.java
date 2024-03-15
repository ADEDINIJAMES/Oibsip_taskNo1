package com.example.atmdemoappforoasis.service;

import com.example.atmdemoappforoasis.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

public interface UserService extends UserDetailsService {
     String logInUser(LoginDto userDto);
     String logoutUser (HttpServletRequest requestHandlerServlet);
}
