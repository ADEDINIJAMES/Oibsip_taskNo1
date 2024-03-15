package com.example.atmdemoappforoasis.models;

import com.example.atmdemoappforoasis.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private UserRole roles;
    private Boolean isEnabled;
    private String confirmPassword;
    @OneToOne
    private Account account;
    private String address;
    private LocalDateTime confirmationExpiration;

    @OneToMany
    private List<Transactions> transactions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(this.roles.name())));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.isEnabled;
    }
    @JsonIgnore
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching(){
        return password != null && password.equals(confirmPassword);
    }
}


