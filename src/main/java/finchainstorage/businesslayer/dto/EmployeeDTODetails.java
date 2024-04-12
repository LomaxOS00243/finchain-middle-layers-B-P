package finchainstorage.businesslayer.dto;

import finchainstorage.businesslayer.inmemory.InMemoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDTODetails implements UserDetailsService {

    @Autowired
    private InMemoryServices inMemoryService;

    @Override
    public EmployeeDTO loadUserByUsername(String employeeName) throws UsernameNotFoundException {

        EmployeeDTO employeeDetails = inMemoryService.getEmployee(employeeName);

        if (employeeDetails == null) {
            throw new UsernameNotFoundException("Employee not found");
        }
        return employeeDetails;

    }
}
