package com.example.atmdemoappforoasis.controller;
import com.example.atmdemoappforoasis.dto.LoginDto;
import com.example.atmdemoappforoasis.dto.UserDto;
import com.example.atmdemoappforoasis.serviceImplementation.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserServiceImpl userService;
    @Autowired
    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }
  @PostMapping("/register")
  public ResponseEntity<String> SignUp (@RequestBody UserDto userDto){
String response = userService.registerUser(userDto);
return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
String result = userService.logInUser(loginDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/confirmUser/{email}")
    public ResponseEntity<String> confirmUser (@PathVariable String email){
        String response = userService.confirmUser(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/quit")
    public ResponseEntity<String>logout (HttpServletRequest request){
String response = userService.logoutUser(request);
return ResponseEntity.ok(response);
    }


}
