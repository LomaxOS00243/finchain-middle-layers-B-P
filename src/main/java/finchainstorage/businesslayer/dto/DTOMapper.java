package finchainstorage.businesslayer.dto;


import finchainstorage.businesslayer.model.Employees;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {
    public static EmployeeDTO fromEmployee(Employees employee) {
        return new EmployeeDTO(employee.getEmployeeId(), employee.getName(), employee.getPassword());
    }

}
