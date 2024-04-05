package finchainstorage.persistancelayer.gateway.chaincodeservices;

import finchainstorage.businesslayer.model.Employees;

public interface EmployeeChaincodeService {
    Employees createEmployeeAccount(Employees employee);

    void loginEmployee(Employees employee);
}
