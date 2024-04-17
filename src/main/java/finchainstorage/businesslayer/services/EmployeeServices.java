package finchainstorage.businesslayer.services;

import finchainstorage.businesslayer.dto.EmployeeDTOLogin;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.dto.EmployeeDTO;


public interface EmployeeServices {

    String createAccount(Employees employee);

    boolean login(EmployeeDTOLogin eLoginDto);

    void uploadDocument( String documentId, String employeeId, String documentName);

    void verifyLoginTransaction(EmployeeDTOLogin eLoginDto);

    EmployeeDTO findEmployee(EmployeeDTOLogin eLoginDto);


    void addEmployee(String employeeId,  EmployeeDTO employeeDetails);

}
