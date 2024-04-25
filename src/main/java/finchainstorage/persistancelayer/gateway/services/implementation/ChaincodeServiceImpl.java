package finchainstorage.persistancelayer.gateway.services.implementation;

import finchainstorage.businesslayer.model.Employees;
import finchainstorage.persistancelayer.gateway.services.ChaincodeService;
import finchainstorage.persistancelayer.gateway.exception.GatewayApiException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 *
 */
@Service
public class ChaincodeServiceImpl implements ChaincodeService {
    @Override
    public String createAccount(Employees employee, Contract contract) {

        String hashRecoveryPhase = UtilityChaincodeServiceImpl.generateHashValue(employee.getRecoveryPhrase());
        try {
            byte[] response = contract.submitTransaction("createEmployeeAccount", employee.getEmployeeId(), employee.getName(),
                                                                                        employee.getPassword(), hashRecoveryPhase);

            return new String(response, StandardCharsets.UTF_8);

        } catch (ContractException | TimeoutException | InterruptedException e) {
            throw new GatewayApiException( "Error from the network - Failed to create an account", e);
        }
    }

    @Override
    public void uploadDocument(String documentId, String employeeId, String documentName, Contract contract) {

        String documentMetadata = UtilityChaincodeServiceImpl.generateHashValue(documentId);

        try {
            contract.submitTransaction("uploadDocument", documentMetadata, employeeId, documentName);
        } catch (ContractException | TimeoutException | InterruptedException e) {
            throw new GatewayApiException("Error from the network - Failed to upload document", e);
        }
    }


}