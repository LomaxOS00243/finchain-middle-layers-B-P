package finchainstorage.businesslayer.inmemory;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryServices {
    private static final Map<String, EmployeeDTO> registeredEmployees = new ConcurrentHashMap<>();
    public void addEmployee(String employeeId,  EmployeeDTO employeeDetails) {

        registeredEmployees.put(employeeId, employeeDetails);
    }

    public EmployeeDTO getEmployee(String employeeId) {
        return registeredEmployees.get(employeeId);
    }
}
