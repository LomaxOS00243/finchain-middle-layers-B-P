package finchainstorage.businesslayer.dto;


import finchainstorage.businesslayer.model.Employees;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDtoMapper {
    public static EmployeeDTO fromEmployee(Employees employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public static Employees toEmployee(EmployeeDTO employeeDTO) {
        Employees employee = new Employees();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
