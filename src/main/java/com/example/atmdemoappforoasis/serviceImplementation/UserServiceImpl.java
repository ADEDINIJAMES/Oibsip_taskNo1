package com.example.atmdemoappforoasis.serviceImplementation;

import com.example.atmdemoappforoasis.dto.LoginDto;
import com.example.atmdemoappforoasis.dto.UserDto;
import com.example.atmdemoappforoasis.enums.UserRole;
import com.example.atmdemoappforoasis.exception.UserNotVerifiedException;
import com.example.atmdemoappforoasis.models.Users;
import com.example.atmdemoappforoasis.repository.UserRepository;
import com.example.atmdemoappforoasis.service.UserService;
import com.example.atmdemoappforoasis.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailServiceImpl;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, EmailServiceImpl emailServiceImpl) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not Found"));
    }

    @Override
    public String logInUser(LoginDto userDto) {
        UserDetails user = loadUserByUsername(userDto.getEmail());

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException("User is not verified, check email to Verify Registration");
        }

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UserNotVerifiedException("Username and Password is Incorrect");
        }

        return jwtUtils.createJwt.apply(user);
    }


    public String registerUser (UserDto userDto){
       try {
           Users user = new Users();
           user.setEmail(userDto.getEmail());
           user.setFirstName(userDto.getFirstName());
           user.setPassword(userDto.getPassword());
           user.setAddress(userDto.getAddress());
           user.setRoles(UserRole.CUSTOMER);
           user.setPassword(passwordEncoder.encode(userDto.getPassword()));
           user.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));
           user.setIsEnabled(false);
           user.setLastName(userDto.getLastName());
           user.setPhoneNumber(userDto.getPhoneNumber());
           if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
               LocalDateTime expirationTime = LocalDateTime.now().plus(24, ChronoUnit.HOURS);
               user.setConfirmationExpiration(expirationTime);
               Users users = userRepository.save(user);
               String message = "Click" + " " + "http://localhost:8081/api/v1/auth/confirmUser/" + user.getEmail() + "   " + " " + "expires after 24 hours";
               emailServiceImpl.sendMail(users.getEmail(), "Confirm Your Email", message);
               return "signed Up successful, refer to your email to confirm your account";
           }
           return "password and confirmPassword different";
       }catch (DuplicateKeyException e){
           e.printStackTrace();
           return "duplicate key value violates unique constraint";
       }catch (Exception ex){
           ex.printStackTrace();
           return "An Error occurred";
       }
       }

    public String confirmUser ( String email){
        Boolean user = userRepository.existsByEmail(email);
        if(user){
            Users users = (Users) userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            users.setIsEnabled(true);
            userRepository.save(users);
            return "Email confirmed successfully";
        }
        return "Signup again";
    }
public String logoutUser (HttpServletRequest request){
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users!= null){
            SecurityContextHolder.getContext().setAuthentication(null);
           SecurityContextHolder.clearContext();
           request.getSession().invalidate();
           return "logout successful";
        }
        return "you are not logged in";
}



}
