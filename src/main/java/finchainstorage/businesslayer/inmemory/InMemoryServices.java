package finchainstorage.businesslayer.inmemory;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryServices {
    private final static Map<String, EmployeeDTO> registeredEmployees = new ConcurrentHashMap<>();
    public void addEmployee(Map<String, EmployeeDTO> reg, String employeeId,  EmployeeDTO employeeDetails) {

        reg.put(employeeId, employeeDetails);
    }
    public boolean checkEmployee(String employeeId,  String password) {
        EmployeeDTO employee = registeredEmployees.get(employeeId);
        return employee != null && employee.getPassword().equals(password);
    }

    public EmployeeDTO getEmployee(String employeeId) {
        return registeredEmployees.get(employeeId);
    }
}
