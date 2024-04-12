package finchainstorage.persistancelayer.gateway.chaincodeservices.implementation;

import finchainstorage.businesslayer.model.Employees;
import finchainstorage.persistancelayer.gateway.chaincodeservices.ChaincodeService;
import finchainstorage.persistancelayer.gateway.exception.GatewayApiException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.stereotype.Repository;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 *
 */
@Repository
public class ChaincodeServiceImpl implements ChaincodeService {
    @Override
    public String createAccount(Employees employee, Contract contract) {

        String hashRecoveryPhase = UtilityChaincodeServiceImpl.generateHashValue(employee.getRecoveryPhrase());
        try {
            byte[] response = contract.submitTransaction("createEmployeeAccount", employee.getEmployeeId(), employee.getName(), employee.getPassword(), hashRecoveryPhase);

            return new String(response, StandardCharsets.UTF_8);

        } catch (ContractException | TimeoutException | InterruptedException e) {
            throw new GatewayApiException( "Error from the network - Failed to create an account", e);
        }
    }

    @Override
    public void uploadDocument(String employeeId, String documentName, String documentHash, Contract contract) {
        try {
            contract.submitTransaction("uploadDocument", employeeId, documentName, documentHash);
        } catch (ContractException | TimeoutException | InterruptedException e) {
            throw new GatewayApiException("Error from the network - Failed to upload document", e);
        }
    }


}