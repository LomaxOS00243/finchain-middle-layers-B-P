package finchainstorage.businesslayer.services;

import finchainstorage.businesslayer.dto.EmployeeDTOLogin;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.dto.EmployeeDTO;


public interface EmployeeAuthenticationService {

    String createAccount(Employees employee);
    EmployeeDTO findEmployee(EmployeeDTOLogin eLoginDto);

    boolean login(EmployeeDTOLogin eLoginDto);

    void verifyLoginTransaction(EmployeeDTOLogin eLoginDto);
}
