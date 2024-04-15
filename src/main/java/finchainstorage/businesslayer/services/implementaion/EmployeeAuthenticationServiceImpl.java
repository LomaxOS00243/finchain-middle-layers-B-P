package finchainstorage.businesslayer.services.implementaion;

import finchainstorage.businesslayer.dto.EmployeeDTOLogin;
import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.exception.BusinessApiException;
import finchainstorage.businesslayer.inmemory.InMemoryServices;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.services.EmployeeAuthenticationService;
import finchainstorage.persistancelayer.gateway.chaincodeservices.implementation.ChaincodeServiceImpl;
import finchainstorage.persistancelayer.gateway.chaincodeservices.implementation.UtilityChaincodeServiceImpl;
import finchainstorage.persistancelayer.gateway.network.NetworkConnector;
import org.hyperledger.fabric.gateway.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAuthenticationServiceImpl implements EmployeeAuthenticationService {


    private final ChaincodeServiceImpl chaincodeServer;
    private final UtilityChaincodeServiceImpl utilityChaincodeServer;
    private Contract contract;
    private final InMemoryServices inMemoryService;

    @Autowired
    public EmployeeAuthenticationServiceImpl(ChaincodeServiceImpl chaincodeServer, UtilityChaincodeServiceImpl utilityChaincodeServer, InMemoryServices inMemoryService) {
        this.chaincodeServer = chaincodeServer;
        this.utilityChaincodeServer = utilityChaincodeServer;
        this.inMemoryService = inMemoryService;
    }

    @Override
    public String createAccount(Employees employee) {

        //Establish the connection with the requester digital identity
        contract = NetworkConnector.getConnected(employee.getEmployeeId());

        //verify duplication
        utilityChaincodeServer.verifyDuplication(employee.getEmployeeId(), contract);

        //verify permissions
        utilityChaincodeServer.verifyAccessPermission(contract);

        //Listen to the events
        utilityChaincodeServer.eventsListener(contract);

        return chaincodeServer.createAccount(employee, contract);
    }
   /* @Override
    public EmployeeDTO findEmployee(EmployeeDTOLogin eLoginDto) {

        boolean isEmployeeExist = inMemoryService.checkEmployee(eLoginDto.getEmployeeId(), eLoginDto.getPassword());

        if (!isEmployeeExist) {
            throw new BusinessApiException("Employee not found: You must to register first");
            //Redirect to the registration page
        }
        return inMemoryService.getEmployee(eLoginDto.getEmployeeId());
    }*/
    @Override
    public void verifyLoginTransaction(EmployeeDTOLogin eLoginDto) {

        //call login method to send to the blockchain network for verification
        boolean isTransactionValid = login(eLoginDto);

        if (!isTransactionValid) {
            throw new BusinessApiException("Access Denied: You must register first");
            //Redirect to the registration page
        }

    }
    //login method to send to the blockchain network for verification
    @Override
    public boolean login(EmployeeDTOLogin eLoginDto) {

        //Establish the connection with the requester digital identity
        contract = NetworkConnector.getConnected(eLoginDto.getEmployeeId());

        //verify permissions
        utilityChaincodeServer.verifyAccessPermission(contract);

        //verify the password
        utilityChaincodeServer.verifyPassword(contract, eLoginDto.getEmployeeId(), eLoginDto.getPassword());

        return true;
    }


}
