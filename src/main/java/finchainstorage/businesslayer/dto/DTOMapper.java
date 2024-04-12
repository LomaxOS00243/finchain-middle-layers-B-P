package finchainstorage.businesslayer.dto;


import finchainstorage.businesslayer.model.Employees;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {
    public static EmployeeDTO fromEmployee(Employees employee) {
        EmployeeDTO eReqDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, eReqDTO);
        return eReqDTO;
    }

    public static Employees toEmployee(EmployeeDTO employeeDto) {
        Employees employee = new Employees();
        BeanUtils.copyProperties(employeeDto, employee);
        return employee;
    }
}
