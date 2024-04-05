package finchainstorage.businesslayer.services.implementaion;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.dto.EmployeeDtoMapper;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.services.EmployeeService;
import finchainstorage.persistancelayer.gateway.chaincodeservices.implementation.ChaincodeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final ChaincodeServiceImpl chaincodeService = new ChaincodeServiceImpl();
    @Override
    public EmployeeDTO createAccount(Employees employee) {
        return EmployeeDtoMapper.fromEmployee(chaincodeService.createEmployeeAccount(employee));
    }
}
