package finchainstorage.persistancelayer.gateway.chaincodeservices;

import finchainstorage.businesslayer.model.Employees;
import org.hyperledger.fabric.gateway.Contract;

public interface ChaincodeService {
    String createAccount(Employees employee, Contract contract);

    void uploadDocument(String employeeId, String documentName, String documentHash, Contract contract);
}
