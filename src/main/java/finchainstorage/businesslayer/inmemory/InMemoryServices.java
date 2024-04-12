package finchainstorage.businesslayer.inmemory;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InMemoryServices {
    private final HashMap<String, EmployeeDTO> registered_employees = new HashMap<>();

    //Add employee details on registration
    public void addEmployee(String employeeName, EmployeeDTO employeeDetails) {
        registered_employees.put(employeeName, employeeDetails);
    }

    //Check if the employee is registered when logging in
    public boolean checkEmployee(String employeeName, String password) {

        return registered_employees.containsKey(employeeName) && registered_employees.get(employeeName).getPassword().equals(password);
    }
    //Get employee details to map to EmployeeResponseDTO
    public EmployeeDTO getEmployee(String employeeName) {
        return registered_employees.get(employeeName);
    }


}
