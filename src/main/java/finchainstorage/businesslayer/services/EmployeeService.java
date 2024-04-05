package finchainstorage.businesslayer.services;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.model.Employees;

public interface EmployeeService {
    EmployeeDTO createAccount(Employees employee);
}
