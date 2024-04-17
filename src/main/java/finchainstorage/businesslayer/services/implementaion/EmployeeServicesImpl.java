package finchainstorage.businesslayer.services.implementaion;

import finchainstorage.businesslayer.dto.EmployeeDTOLogin;
import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.exception.BusinessApiException;
import finchainstorage.businesslayer.inmemory.InMemoryServices;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.services.EmployeeServices;
import finchainstorage.persistancelayer.gateway.services.implementation.ChaincodeServiceImpl;
import finchainstorage.persistancelayer.gateway.services.implementation.UtilityChaincodeServiceImpl;
import finchainstorage.persistancelayer.gateway.network.NetworkConnector;
import org.hyperledger.fabric.gateway.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeServicesImpl implements EmployeeServices {



    private final ChaincodeServiceImpl chaincodeServer;

    private final UtilityChaincodeServiceImpl utilityChaincodeServer;
    private Contract contract;

    private final InMemoryServices inMemoryService;

    @Autowired
    public EmployeeServicesImpl(ChaincodeServiceImpl chaincodeServer, UtilityChaincodeServiceImpl utilityChaincodeServer, InMemoryServices inMemoryService) {
        this.chaincodeServer = chaincodeServer;
        this.utilityChaincodeServer = utilityChaincodeServer;
        this.inMemoryService = inMemoryService;
    }


    @Override
    public String createAccount(Employees employee) {

        //Establish the connection with the requester digital identity
        contract = NetworkConnector.getConnected(employee.getEmployeeId());

        //Transaction to verify duplication
        utilityChaincodeServer.verifyDuplication(employee.getEmployeeId(), contract);

        //Transaction to verify permissions
        utilityChaincodeServer.verifyAccessPermission(contract);

        //Listen to the events to get the response from the blockchain network
        utilityChaincodeServer.eventsListener(contract);

        return chaincodeServer.createAccount(employee, contract);
    }

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

        //Transaction to verify permissions from the blockchain network
        utilityChaincodeServer.verifyAccessPermission(contract);

        //Transaction to verify the password from the blockchain network
        utilityChaincodeServer.verifyPassword(contract, eLoginDto.getEmployeeId(), eLoginDto.getPassword());

        return true;
    }
    @Override
    public void uploadDocument(String documentId, String employeeId, String documentName) {
        //Establish the connection with the requester digital identity
        contract = NetworkConnector.getConnected(employeeId);

        //Transaction to verify permissions from the blockchain network
        utilityChaincodeServer.verifyAccessPermission(contract);

        //Transaction to upload the document details to the blockchain network
        chaincodeServer.uploadDocument(documentId,employeeId, documentName, contract);

        //Listen to the events to get the response from the blockchain network
        utilityChaincodeServer.eventsListener(contract);
    }
    @Override
    public void addEmployee(String employeeId, EmployeeDTO employeeDetails) {
        inMemoryService.addEmployee(employeeId, employeeDetails);
    }
    @Override
    public EmployeeDTO findEmployee(EmployeeDTOLogin eLoginDto) {

        EmployeeDTO employee = inMemoryService.getEmployee(eLoginDto.getEmployeeId());

        if (employee == null) {

            throw new BusinessApiException("Employee not found: You must register first");

        } else if (!employee.getPassword().equals(eLoginDto.getPassword())) {

            throw new BusinessApiException("Incorrect password");
        }
        return employee;
    }
}
