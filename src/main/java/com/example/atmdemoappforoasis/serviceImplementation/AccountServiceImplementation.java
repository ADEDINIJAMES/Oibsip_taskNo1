package com.example.atmdemoappforoasis.serviceImplementation;

import com.example.atmdemoappforoasis.dto.AccountCreationDto;
import com.example.atmdemoappforoasis.enums.Status;
import com.example.atmdemoappforoasis.models.Account;
import com.example.atmdemoappforoasis.models.Users;
import com.example.atmdemoappforoasis.repository.AccountRepository;
import com.example.atmdemoappforoasis.repository.UserRepository;
import com.example.atmdemoappforoasis.service.AccountService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

@Service
public class AccountServiceImplementation implements AccountService {
    private final UserRepository userRepository;
    private final EmailServiceImpl emailServiceImpl;
    private final AccountRepository accountRepository;

@Autowired
    public AccountServiceImplementation(UserRepository userRepository, EmailServiceImpl emailServiceImpl, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.emailServiceImpl = emailServiceImpl;
        this.accountRepository = accountRepository;
    }


    @Override
    public String createAccount(AccountCreationDto accountDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Users user = (Users) userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            validateAccountDto(accountDto);
            Account account = new Account();
            account.setUser(user);
            account.setAccountName(user.getFirstName() + " " + user.getLastName());
            account.setAccountNo(generateAccountNumber());
            account.setAccountType(accountDto.getAccountType());
            account.setBalance(BigDecimal.ZERO);
            account.setModified(null);
            account.setStatus(Status.ACTIVE);
           Account account1= accountRepository.save(account);
           user.setAccount(account1);
           userRepository.save(user);
emailServiceImpl.sendMail(username, "Account Credentials", " Your Account No is"+ account1.getAccountNo()+ "  "+" Account Name:"+ " "+ account1.getAccountName() );
            return "Account created successfully!!"+ " "+" Your Account No is"+ account1.getAccountNo()+ "  "+" Account Name:"+ " "+ account1.getAccountName();
        } catch (UsernameNotFoundException ex) {
            return "Failed to create account: User not found";
        } catch (ValidationException ex) {
            return "Failed to create account: " + ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Failed to create account: An unexpected error occurred. Please try again later." ;       }
    }

    private void validateAccountDto(AccountCreationDto accountDto) throws ValidationException {
        if (accountDto.getAccountType() == null) {
            throw new ValidationException("Account type cannot be empty");
        }
        // Add more validation logic as needed
    }
    @Override
    public String getBalance (){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Users user = (Users) userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("username not found"));
    Account account = accountRepository.findAccountByAccountNo(user.getAccount().getAccountNo()).orElseThrow(()-> new UsernameNotFoundException("Account not found"));
    if(Objects.equals(user.getEmail(), account.getUser().getEmail())) {
        return "Your account Balance is: " + account.getBalance() + ", " + " account Name: " + account.getAccountName();
    }
    return "you are not permitted";
    }

    public static String generateAccountNumber() {
            Random random = new Random();
            StringBuilder accountNumber = new StringBuilder();

            // Generate 10 random digits
            for (int i = 0; i < 10; i++) {
                int digit = random.nextInt(10); // Generate a random digit between 0 and 9
                accountNumber.append(digit);
            }

            return accountNumber.toString();
        }
    }
