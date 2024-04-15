package finchainstorage.businesslayer.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//class t use to create a session

public class EmployeeDTO implements UserDetails {

    private final String employeeId;
    private final String name;
    private final String password;


    public EmployeeDTO(String employeeId, String name, String password)  {

        this.employeeId = employeeId;
        this.name = name;
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("EMPLOYEE"));
    }

    @Override
    public String getUsername() {
        return name;
    }
    @Override
    public String getPassword() {
        return password;
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
    public boolean isEnabled() {
        return true;
    }
}
