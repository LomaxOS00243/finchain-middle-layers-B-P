package finchainstorage.persistancelayer.gateway.services;

import finchainstorage.businesslayer.model.Employees;
import org.hyperledger.fabric.gateway.Contract;

public interface ChaincodeService {
    String createAccount(Employees employee, Contract contract);

    void uploadDocument(String documentId, String employeeId, String documentName, Contract contract);
}
